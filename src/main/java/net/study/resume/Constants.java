package net.study.resume;

public class Constants {

    public static final String[] EMPTY_ARRAY = {};

    public static class UI {

        public static final int MAX_PROFILES_PER_PAGE = 5;
    }

    public static class AuthProvider {

        public static final String GOOGLE = "google";
        public static final String FACEBOOK = "facebook";
        public static final String GITHUB = "github";
        public static final String LOCAL = "local";
    }

    public enum UIImageType {

        AVATARS(110, 110, 400, 400),

        CERTIFICATES(161, 100, 900, 400);

        private final int smallWidth;
        private final int smallHeight;
        private final int largeWidth;
        private final int largeHeight;

        UIImageType(int smallWidth, int smallHeight, int largeWidth, int largeHeight) {
            this.smallWidth = smallWidth;
            this.smallHeight = smallHeight;
            this.largeWidth = largeWidth;
            this.largeHeight = largeHeight;
        }
        public String getFolderName() {
            return name().toLowerCase();
        }
        public int getSmallWidth() {
            return smallWidth;
        }
        public int getSmallHeight() {
            return smallHeight;
        }
        public int getLargeWidth() {
            return largeWidth;
        }
        public int getLargeHeight() {
            return largeHeight;
        }
    }
}
