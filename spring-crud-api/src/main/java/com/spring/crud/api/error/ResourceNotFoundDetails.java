package com.spring.crud.api.error;

public class ResourceNotFoundDetails extends ErrorDetail {

    public static final class Builder {
        private String title;
        private int status;
        private String detail;
        private long timestamp;
        private String developerMessage;

        private Builder() {
        }

        public static ResourceNotFoundDetails.Builder newBuilder() {
            return new ResourceNotFoundDetails.Builder();
        }

        public ResourceNotFoundDetails.Builder title(String title) {
            this.title = title;
            return this;
        }

        public ResourceNotFoundDetails.Builder status(int status) {
            this.status = status;
            return this;
        }

        public ResourceNotFoundDetails.Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public ResourceNotFoundDetails.Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ResourceNotFoundDetails.Builder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }


        public ResourceNotFoundDetails build() {
            ResourceNotFoundDetails resourceNotFoundDetails = new ResourceNotFoundDetails();
            resourceNotFoundDetails.setDeveloperMessage(this.developerMessage);
            resourceNotFoundDetails.setTitle(this.title);
            resourceNotFoundDetails.setDetail(this.detail);
            resourceNotFoundDetails.setTimestamp(this.timestamp);
            resourceNotFoundDetails.setStatus(this.status);
            return resourceNotFoundDetails;
        }
    }
}
