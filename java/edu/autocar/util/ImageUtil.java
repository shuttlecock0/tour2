package edu.autocar.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	// byte 배열에서 thumbnail byte 배열 생성
	public static byte[] thumb(byte[] data) throws Exception {
		return thumb(data, 100, 100);
	}

	public static byte[] thumb(byte[] data, int width, int height) throws Exception {
		try (ByteArrayInputStream in = new ByteArrayInputStream(data);
				ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Thumbnails.of(in).size(width, height).crop(Positions.CENTER).toOutputStream(out); // 스트림으로 출력하기
			return out.toByteArray();
		}
	}

	// 파일을 원본으로 해서 thumbnail 파일 만들기
	public static void thumb(File srcFile, File thumbFile) throws Exception {
		thumb(srcFile, thumbFile, 100, 100);
	}

	public static void thumb(File srcFile, File thumbFile, int width, int height) throws Exception {
		Thumbnails.of(srcFile).size(width, height).crop(Positions.CENTER).outputFormat("jpg").toFile(thumbFile);//파일로 저장하기
	}
}
