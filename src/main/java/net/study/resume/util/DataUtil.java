package net.study.resume.util;

import net.study.resume.entity.Certificate;
import java.util.*;

public class DataUtil {

	public static List<String> getCertificateImageUrls(List<Certificate> certificates) {
		List<String> res = new ArrayList<>(certificates.size()*2);
		for(Certificate certificate : certificates) {
			res.add(certificate.getLargeUrl());
			res.add(certificate.getSmallUrl());
		}
		return res;
	}

	public static String normalizeName(String name) {
		return name.trim().toLowerCase();
	}

	public static String capitalizeName(String name) {
		return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
	}

	public static String generateRandomString(String alphabet, int letterCount) {
		Random r = new Random();
		StringBuilder uid = new StringBuilder();
		for (int i = 0; i < letterCount; i++) {
			uid.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		return uid.toString();
	}
	
	public static String generateNewToken() {
		return UUID.randomUUID().toString().replace("-", "");
	}


}
