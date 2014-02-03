/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.au.griffith;

public class GriffithBlackboardProvider {/* implements BlackboardProvider,
                                                   MobileProviderLocaliser,
                                                   LectureDownloader {

    private static final Pattern semesterFromCourseIdRegex = Pattern.compile("[^_]*?_(\\d*)");
    private static final Pattern myCourseNameFromBbName = Pattern.compile("(.*) ");
    private static final DateFormat lectureTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat unknowStreamDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int UQ_CLIENT_ID = 599;

    static {
        lectureTimeFormat.setTimeZone(TimeZone.getTimeZone("Australia/Brisbane"));
        unknowStreamDateFormat.setTimeZone(TimeZone.getTimeZone("Australia/Brisbane"));
    }
    private AbstractHttpClient httpClient = new DefaultHttpClient();
    private BlackboardMobileProvider mobileProvider = null;

    public GriffithBlackboardProvider() {
    }

    @Override
    public String getProviderName() {
        return "Griffith University";
    }

    @Override
    public Blackboard createBlackboard() {
        return new Blackboard();
    }

    @Override
    public Image getProviderImage() {
        return null;
    }

    @Override
    public LoginExecuter getLoginExecuter() {
        if (createProvider()) {
            return mobileProvider.getLoginExecuter();
        }
        return null;
    }

    @Override
    public SubjectLocator getSubjectLocator() {
        if (createProvider()) {
            return mobileProvider.getSubjectLocator();
        }
        return null;
    }

    @Override
    public LectureLocator getLectureLocator() {
        if (createProvider()) {
            return mobileProvider.getLectureLocator();
        }
        return null;
    }

    private boolean createProvider() {
        if (mobileProvider != null) {
            return true;
        } else {
            if (BlackboardMobileProvider.checkClientIdSupported(UQ_CLIENT_ID)) {
                mobileProvider = BlackboardMobileProvider.createProvider(UQ_CLIENT_ID, this);
                if (mobileProvider != null) {
                    httpClient = mobileProvider.getHttpClient();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String getCourseName(String name) {
        final Matcher m = myCourseNameFromBbName.matcher(name);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    @Override
    public int getSemesterNumber(String name, String courseId, String bbid, Date enrollmentdate) {
        final Matcher matcher = semesterFromCourseIdRegex.matcher(courseId);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            // Not a true course/not supported. discard this course.
            return Integer.MIN_VALUE;
        }
    }

    @Override
    public Date getLectureTime(URI u, String title, String xmlData) {
        System.out.println("title: " + title);
        String date = StringUtil.regex(title, "(.*?)\\s+-\\s+", 1, null);
        try {
            if (date != null) {
                synchronized (lectureTimeFormat) {
                    return lectureTimeFormat.parse(date);
                }
            } else {
                return null;
            }
        } catch (ParseException ex) {
            ExceptionReporter.reportException(ex, "The string \"" + date + "\" from \"" + title + "\" caused an error");
            return null;
        } catch (NumberFormatException ex) {
            ExceptionReporter.reportException(ex, "The string \"" + date + "\" from \"" + title + "\" caused an error");
            return null;
        }
    }

    @Override
    public List<Stream> getLectureStream(URI u, String title, String rawXml, Subject s) {
        final Date lectureTime = getLectureTime(u, title, null);
        synchronized (unknowStreamDateFormat) {
            return Arrays.asList(new Stream(unknowStreamDateFormat.format(lectureTime), (int) lectureTime.getTime()));
        }
    }

    @Override
    public LectureDownloader getLectureDownloader() {
        return this;
    }

    @Override
    public List<String> getVideoFileNames(URI u, String title, Date d, Subject sub, List<Stream> streams) {
        return Arrays.asList(sub.getFolderName() + StringUtil.formatDate(" ~ yyyy-MM-dd HH.mm.'m4v'", d));
    }

    @Override
    public List<String> getAudioFileNames(URI u, String title, Date d, Subject sub, List<Stream> streams) {
        return Arrays.asList(sub.getFolderName() + StringUtil.formatDate(" ~ yyyy-MM-dd HH.mm.'mp3'", d));
    }

    @Override
    public String getCourseFileName(String name, String courseName) {
        return StringUtil.regex(courseName, "([A-Z]{4}[1-9][0-9]{3})", 1, courseName.replace("/", " ").replace("\\", " "));
    }

    @Override
    public ActionResult downloadLectureTo(Blackboard b, Subject s, Lecture l, boolean video, File[] f) throws IOException {

        if (lectureExists(b, s, l, video, f)) {
            return ActionResult.SUCCEDED;
        }
        URI seamlessLoginURI = l.getUrl();
        HttpGet get = new HttpGet(seamlessLoginURI);
        HttpResponse r = httpClient.execute(get);
        EntityUtils.consumeQuietly(r.getEntity());

        String downloadUrl = URLUtil.decodeUrlParam(StringUtil.regex(l.getUrl().getQuery(), "&url=([^&]*)", 1, ""));
        if (downloadUrl.isEmpty()) {
            return ActionResult.FAILED;
        }
        downloadUrl += "/mediacontent." + (video ? "m4v" : "mp3");
        System.out.println("URL: " + downloadUrl);
        get = new HttpGet(downloadUrl);
        r = httpClient.execute(get);
        if (r.getEntity().getContentType().getValue().startsWith("text/html")) {
            //return ActionResult.FAILED;
            HttpPost post = new HttpPost(new URL(new URL(downloadUrl), "/ess/echo/j_spring_security_check").toString());
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("j_username", b.getUsername()));
            formparams.add(new BasicNameValuePair("j_password", b.getPassword()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.setEntity(entity);
            httpClient.execute(post);
            r = httpClient.execute(get);
        }
        System.out.println("GetHeaders");

        for (Header header : get.getAllHeaders()) {
            System.out.println("Header: " + header);
        }
        System.out.println("Responce");

        for (Header header : r.getAllHeaders()) {
            System.out.println("Header: " + header);
        }
        File f0 = File.createTempFile("DLect", FilenameUtils.getName(f[0].toString()));
        f0.deleteOnExit();
        IOUtils.copy(r.getEntity().getContent(), new FileOutputStream(f0));
        for (int i = 0; i < f.length; i++) {
            FileUtils.copyFile(f0, f[i]);
        }
        f0.delete();
        return ActionResult.SUCCEDED;
    }

    @Override
    public boolean lectureExists(Blackboard b, Subject s, Lecture l, boolean video, File[] f) throws IOException {
        for (File file : f) {
            System.out.println(file + ": " + (file.exists() ? "Exists" : "Non-Existant"));
            if (!file.exists()) {
                return false;
            }
        }
        return true;
    } */
}
