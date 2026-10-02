# iNotes – Ứng dụng quản lý ghi chú (Strategy Pattern)

Java Servlet + JSP (Maven, Tomcat 9). Lưu trữ **MySQL** hoặc **File txt**, chuyển đổi ngay lúc chạy (runtime) trên giao diện.

## Cấu trúc

```
inotes/
├── database/inotes_db.sql              # Script tạo CSDL + dữ liệu mẫu
├── pom.xml
└── src/main
    ├── java/com/inotes
    │   ├── model/       Note, NoteType
    │   ├── strategy/    NoteStrategy (interface), NoteDBStrategy, NoteFileStrategy, NoteException
    │   ├── service/     NoteManagement (Context: setStrategy, searchNotes, addNote, deleteNote)
    │   ├── controller/  NoteServlet
    │   └── util/        DBConnection
    ├── resources/db.properties(.example)
    └── webapp/
        ├── css/style.css
        └── WEB-INF/views/ list.jsp, form.jsp, view.jsp
```

## Chạy dự án

1. **Yêu cầu:** JDK 11+, Maven, Tomcat 9 (javax.servlet), MySQL 8 (nếu dùng DB).
2. **MySQL:** copy `db.properties.example` → `db.properties` (file này bị .gitignore), chạy `database/inotes_db.sql`, rồi sửa `src/main/resources/db.properties` (user/password).
3. **Build:** `mvn clean package` → deploy `target/inotes.war` vào Tomcat 9
   (hoặc cấu hình Tomcat trong IntelliJ/Eclipse/NetBeans, context path `/inotes`).
4. Mở: `http://localhost:8080/inotes/`

> Mặc định chạy ở chế độ **File** (`~/inotes-data/notes.txt`, định dạng `id|title|content|typeId`).
> Dùng ô chọn “Lưu trữ hiện tại” ở góc trên để chuyển sang **MySQL** (sẽ báo lỗi và giữ nguyên chế độ cũ nếu không kết nối được).

> Không dùng Tomcat 10+ (namespace `jakarta`) trừ khi đổi `javax.servlet` → `jakarta.servlet`.

## Đẩy lên Git

```bash
cd inotes
git init
git add .
git commit -m "feat: iNotes with Strategy Pattern (MySQL/File)"
git branch -M main
git remote add origin <URL_REPO_CUA_BAN>
git push -u origin main
```
