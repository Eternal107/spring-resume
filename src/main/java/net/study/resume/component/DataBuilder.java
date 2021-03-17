package net.study.resume.component;

import net.study.resume.util.DataUtil;
import org.springframework.stereotype.Component;


@Component
public class DataBuilder  {

	public String buildCertificateName(String fileName) {
		if (fileName == null) {
			return null;
		}
		int point = fileName.lastIndexOf('.');
		if (point != -1) {
			fileName = fileName.substring(0, point);
		}
		return DataUtil.capitalizeName(fileName);
	}
}
