//정규식 모음
const email =  /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/;
const password = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
const phone_kor = /^[0-9]{3}[-]+[0-9]{4}[-]+[0-9]{4}$/;

/**
 * 모잠비크 어드민 유효성 검사 유틸
 * Mozates Admin Validation Utils JS
 */
 var validationChecker = {
	 
	/**
     * 공백체크 공백시 false
     * @param str
     * @returns {boolean}
     */
	 isNull : function (str){
		 if(str != null && str != ""){
			 return true;
		 }
		 return false;
	 },
	 /**
	  * 정규식 유효성 검사
	  * @param str
	  * @param type
	  * @return {boolean}
	  */
	 isRegexValidation : function (str,type){
		 switch(type){
			 case "EMAIL" :
				if(email.test(str)){
					return true
				} else { 
					return false;
				}
			 case "PASSWORD":
				 if(password.test(str)){
					 return true;
				 } else {
					 return false;
				 }
			 case "PHONE":
				 if(phone_kor.test(str)){
					 return true;
				 } else {
					return false; 
				 }
			 default :
			 return false;
		 }
	 },
	 /**
	  * 비밀번호 확인
	  * @param pw
	  * @param pwChk
	  * @return {boolean}
	  */
	 passwordCheck : function(pw,pwChk){
		 if((!pw)|| (pw != pwChk)){
			 return true;
		 } else {
			 return false
		 }
	 }
 }
 