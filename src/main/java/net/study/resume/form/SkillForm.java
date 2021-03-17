package net.study.resume.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.study.resume.entity.Skill;
import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class SkillForm {
    @Valid
    private List<Skill> items;

    public SkillForm(List<Skill> items) {
        this.items = items;
    }

}
