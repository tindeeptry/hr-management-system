# hr-management-system
Ứng dụng quản lý nhân sự có tích hợp chấm công nhận diện khuôn mặt
# 🏢 HR Management System (HRApp)

---

## 📌 Giới thiệu Dự án
Dự án được xây dựng nhằm tối ưu hóa quy trình quản lý nhân sự cho doanh nghiệp, kết hợp công nghệ AI nhận diện khuôn mặt chống giả mạo (Anti-Spoofing) để tự động hóa quá trình chấm công chính xác và minh bạch.

* **Backend Server (Production):** [https://hr-management-system-px0i.onrender.com/](https://hr-management-system-px0i.onrender.com/)
* **Mobile App:** Android App (Kotlin / Jetpack Compose)

---

## 🛠 Kiến trúc & Công nghệ Sử dụng

### 1. Backend Service
* **Framework:** NestJS (TypeScript)
* **Database:** MySQL
* **Deployment:** Render Platform
* **Timezone:** Asia/Ho_Chi_Minh (`UTC+7`)

### 2. AI / Face Recognition Module
* **Language:** Python
* **Features:** Anti-spoofing detection (Chống giả mạo khuôn mặt bằng ảnh/video), Face Matching.

### 3. Mobile Application (Android)
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose / Android SDK
* **Network:** Retrofit2 / OkHttp (kết nối RESTful API với Backend)

---

## ✨ Tính năng Chính

- [x] **Quản lý Tài khoản & Phân quyền:** Đăng nhập, phân quyền Nhân viên / Quản lý (Admin).
- [x] **Quản lý Hồ sơ Nhân viên:** Thêm, sửa, cập nhật thông tin và dữ liệu khuôn mặt.
- [x] **Chấm công Nhận diện Khuôn mặt:**
  - Chụp ảnh kiểm tra qua app Android.
  - Tự động phát hiện giả mạo (Anti-Spoofing).
  - Ghi nhận thời gian Check-in / Check-out thực tế theo múi giờ Việt Nam.
- [x] **Quản lý Bảng công & Thống kê:** Xem lịch sử chấm công, báo cáo theo ngày/tháng.

---

## 🚀 Hướng dẫn Cài đặt 
## 📥 Tải ứng dụng (Android APK)
* 🚀 **Phiên bản mới nhất (v1.0):** [Tải ngay file HrApp.apk](https://github.com/tindeeptry/hr-management-system/releases/download/v1.0/HrApp.apk)
* * **Tài khoản Admin / Manager:**
  * **Email / Tài khoản:** `admin@hrapp.com`
  * **Mật khẩu:** `123456`
* **Tài khoản Nhân viên (User):**
  * **Email / Tài khoản:** `nv004@hrapp.com` 
  * **Mật khẩu:** `123456`
 
## 💡 Lưu ý Vận hành & Tài khoản Trải nghiệm

> **⚠️ Lưu ý về Server:** 
> Do Backend được triển khai trên hạ tầng miễn phí (Render Platform), hệ thống sẽ tự động chuyển sang trạng thái **"ngủ đông"** nếu không có lưu lượng truy cập sau **15 phút**. 
> Khi mở lại ứng dụng sau thời gian này, thao tác phản hồi đầu tiên (như Đăng nhập) có thể **mất khoảng 1 phút** để máy chủ khởi động lại. Cảm ơn bạn đã kiên nhẫn!
