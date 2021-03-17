package net.study.resume.model;


import net.study.resume.util.DataUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.beans.PropertyEditorSupport;

public enum LanguageType {

	ALL,

	SPOKEN,

	WRITING;

	public String getCaption() {
		return DataUtil.capitalizeName(name());
	}

	public String getDbValue() {
		return name();
	}

	public LanguageType getReverseType() {
		if (this == SPOKEN) {
			return WRITING;
		} else if (this == WRITING) {
			return SPOKEN;
		} else {
			throw new IllegalArgumentException(this+" does not have reverse type");
		}
	}

	public static PropertyEditorSupport getPropertyEditor(){
		return new PropertyEditorSupport(){
			@Override
			public void setAsText(String dbValue) throws IllegalArgumentException {
				setValue(LanguageType.valueOf(dbValue.toUpperCase()));
			}
		};
	}
}
