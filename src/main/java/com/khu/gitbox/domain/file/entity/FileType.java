package com.khu.gitbox.domain.file.entity;

public enum FileType {
	JPG,
	JPEG,
	PNG,
	GIF,
	TXT,
	PDF,
	DOC,
	DOCX,
	XLS,
	XLSX,
	PPT,
	PPTX,
	HWP,
	ZIP,
	RAR,
	ETC;

	public static FileType from(String extension) {
		return switch (extension.toLowerCase()) {
			case "jpg" -> JPG;
			case "jpeg" -> JPEG;
			case "png" -> PNG;
			case "gif" -> GIF;
			case "txt" -> TXT;
			case "pdf" -> PDF;
			case "doc" -> DOC;
			case "docx" -> DOCX;
			case "xls" -> XLS;
			case "xlsx" -> XLSX;
			case "ppt" -> PPT;
			case "pptx" -> PPTX;
			case "hwp" -> HWP;
			case "zip" -> ZIP;
			case "rar" -> RAR;
			default -> ETC;
		};
	}
}
