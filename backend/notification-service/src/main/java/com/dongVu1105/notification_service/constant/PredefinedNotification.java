package com.dongVu1105.notification_service.constant;

import lombok.Getter;

@Getter
public enum PredefinedNotification {
    ACCEPT_REGISTER ("Duyệt đăng kí sự kiện", "Yêu cầu tham gia sự kiện của bạn đã được duyệt. Tên sự kiện: "),
    REJECT_REGISTER("Từ chối đăng kí sự kiện", "Yêu cầu tham gia sự kiện của bạn đã bị từ chối. Tên sự kiện: "),
    CONFIRM_COMPLETION ("Hoàn thành sự kiện" , "Bạn đã hoàn thành sự kiện: "),
    NEW_EVENT ("Sự kiện mới", "Sự kiện mới cần phê duyệt: "),
    ACCEPT_EVENT ("Duyệt sự kiện", "Sự kiện của bạn đã được duyệt. Tên sự kiện: "),
    REJECT_EVENT ("Từ chối sự kiện", "Sự kiện của bạn đã bị từ chối. Tên sự kiện: "),
    NEW_REGISTER ("Đăng ký mới", " đăng ký tham gia sự kiện của bạn"),
    NEW_POST ("Bài viết mới", " đã thêm bài viết mới từ sự kiện: "),
    NEW_REACT ("Lượt thích mới", " đã thích bài viết: "),
    NEW_COMMENT ("Bình luận mới", " đã bình luận bài viết: "),


    ;
    private final String subject;
    private final String content;

     PredefinedNotification(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}
