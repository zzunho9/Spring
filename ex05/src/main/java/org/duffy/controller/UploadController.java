package org.duffy.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.duffy.domain.AttachDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		
		log.info("upload form");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadActionFormPost(MultipartFile[] uploadFiles, Model model) {
		
		String uploadFolder = "/Users/zzun_ho9/upload";
		
		for(MultipartFile file : uploadFiles) {
			
			log.info("--------------------------------");
			log.info("Upload file name :"+file.getOriginalFilename());
			log.info("Upload file size :"+file.getSize());
			
			File saveFile = new File(uploadFolder, file.getOriginalFilename());
			
			try {
				file.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());
			}//end catch
			
		}//end for	
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		
		log.info("upload ajax");
	}
	
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	@PostMapping(value="/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachDTO>> uploadAjaxActionPost(MultipartFile[] uploadFiles) {
		
		List<AttachDTO> list = new ArrayList<AttachDTO>();
		
		String uploadFolder = "/Users/zzun_ho9/upload";
		String uploadFolderPath = getFolder();
		
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("1"+uploadPath);
		
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		
		for(MultipartFile file : uploadFiles) {
			AttachDTO attach = new AttachDTO();
			
			log.info("Upload file name :"+file.getOriginalFilename());
			log.info("Upload file size :"+file.getSize()+"KB");
			log.info("--------------------------------------------------");

			String uploadFileName = file.getOriginalFilename();
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("/")+1);
			log.info("only file name :"+uploadFileName);
			
			attach.setFileName(uploadFileName);
			log.info("uploadFileName :"+uploadFileName);
			
			UUID uuid = UUID.randomUUID();
			
			uploadFileName = uuid + "_"+ uploadFileName;
			
			attach.setUuid(String.valueOf(uuid));
			log.info("uuid"+uuid);
			attach.setUploadPath(uploadFolderPath);
			log.info("uploadFolder"+uploadFolderPath);
			
//			File saveFile = new File(uploadFolder, uploadFileName);

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				file.transferTo(saveFile);
				log.info("uploadPath :"+uploadPath);
				if(checkImage(saveFile)) {
					attach.setImage(true);
					
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"+uploadFileName));
					
					Thumbnailator.createThumbnail(file.getInputStream(), thumbnail, 100, 100);
					
					thumbnail.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}//end catch
			
			list.add(attach);
		}//end for	

		return new ResponseEntity<List<AttachDTO>> (list, HttpStatus.OK);
	}
	
	private boolean checkImage(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@GetMapping(value="/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {
		log.info("file name :"+fileName);
		
		File file = new File("/Users/zzun_ho9/upload/"+fileName);
		log.info(file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath())); // image
			
			result =  new ResponseEntity<byte[]> (FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
//			log.info(Arrays.toString(FileCopyUtils.copyToByteArray(file)));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName){
		
		Resource resource = new FileSystemResource("/Users/zzun_ho9/upload/"+fileName);
		
		if(resource.exists() == false) {
			log.info("resource not exists");
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);	
		}
		
		String resourceName = resource.getFilename();
		
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			String downloadName = "";
			
			if(userAgent.contains("Trident")) {
				log.info("IE...........");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("/", " ");
				log.info(downloadName);
			}
			else if(userAgent.contains("Edge")) {
				log.info("Edge.............");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
				log.info(downloadName);
			}
			else {
				log.info("Chrome........");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
				log.info(downloadName);
			}
			headers.add("Content-Disposition", "attachment; filename="+downloadName);
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@PostMapping(value = "/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String fileType){
		log.info("name: "+fileName);
		log.info("type: "+fileType);
		
		File file;
		
		try {
			file = new File("/Users/zzun_ho9/upload/"+URLDecoder.decode(fileName, "UTF-8"));
			
			file.delete();
			
			if(fileType.equals("image")) {
				String sourceImageName = file.getAbsolutePath().replace("s_", "");
				
				log.info("sourceImageName: "+sourceImageName);
				
				file = new File(sourceImageName);
				
				file.delete();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	
}
