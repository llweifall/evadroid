<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import = "java.io.*,java.util.*, javax.servlet.*" pageEncoding="UTF-8"
         import = "javax.servlet.http.*"
         import = "org.apache.commons.fileupload.*"
         import = "org.apache.commons.fileupload.FileItemFactory"
         import = "org.apache.commons.fileupload.disk.*" 
         import = "org.apache.commons.fileupload.servlet.*"
         import = "org.apache.commons.io.output.*"
         import = "evadroid.model.User"
         import = "evadroid.model.UserProfile"
         import = "evadroid.model.AppProfile"
         import = "java.text.DateFormat"
         import = "java.text.SimpleDateFormat"
         import = "java.util.Date"%>

<%
   File file ;
   int maxFileSize = 50000 * 1024;
   int maxMemSize = 50000 * 1024;
   ServletContext context = pageContext.getServletContext();
   //String filePath = context.getInitParameter("file-upload");
   String filePath = context.getRealPath("/") + "myFile/apk/";
   //String filePath = "/tmp/";

   // Verify the content type
   String contentType = request.getContentType();
   if ((contentType.indexOf("multipart/form-data") >= 0)) {

      DiskFileItemFactory factory = new DiskFileItemFactory();
      // maximum size that will be stored in memory
      factory.setSizeThreshold(maxMemSize);
      // Location to save data that is larger than maxMemSize.

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      // maximum file size to be uploaded.
      upload.setSizeMax( maxFileSize );
      try{ 
         // Parse the request to get file items.
         List fileItems = upload.parseRequest(request);

         // Process the uploaded file items
         Iterator i = fileItems.iterator();

         while ( i.hasNext () ) 
         {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () )  
            {
               // Get the uploaded file parameters
               String fieldName = fi.getFieldName();
               String fileName = fi.getName();
               boolean isInMemory = fi.isInMemory();
               long sizeInBytes = fi.getSize();
               DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
               fileName = df.format(new Date()) + fileName.substring( fileName.lastIndexOf("\\") + 1);

               file = new File(filePath + fileName);
               
               fi.write( file ) ;
               out.print("myFile/apk/"+ fileName);
            }
         }
      }catch(Exception ex) {
        /* out.println("<br>");
         out.println(ex);*/
      }
   }else{
   }
%>