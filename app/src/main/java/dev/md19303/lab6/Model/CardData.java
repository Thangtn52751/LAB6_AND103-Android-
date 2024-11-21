    package dev.md19303.lab6.Model;

    public class CardData {
        private final int imageResId;
        private final String title;
        private final String description;

        public CardData(int imageResId, String title, String description) {
            this.imageResId = imageResId;
            this.title = title;
            this.description = description;
        }

        public int getImageResId() {
            return imageResId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
