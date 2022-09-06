package com.y2gcoder.blog.service.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "로그인 요청")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

	@ApiModelProperty(value = "이메일", notes = "이메일을 입력해주세요.", required = true, example = "member@member.com")
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;

	@ApiModelProperty(
			value = "비밀번호",
			notes = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자(@ $ ! % * # ? &)를 포함해야합니다.",
			required = true,
			example = "!q2w3e4r"
	)
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;
}
