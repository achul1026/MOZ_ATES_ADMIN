/*
* validation 
*/
$.fn.soValid = function(){
	let $wrap = $(this);
	const passwordRegExp = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,32}$/;
	const emailRegExp = /^[a-z0-9A-Z._-]+@[a-z0-9A-Z._-]+\.[a-zA-Z.-]*$/i;
	// latitude: -90 ~ 90. 소수점 15자리
	const latitudeRegex = /^(\+|-)?(?:90(?:(?:\.0{1,15})?)|(?:[0-8]?\d(?:(?:\.\d{1,15})?)))$/;
	// longitude: -180 ~ 180. 소수점 15자리
	const longitudeRegex = /^(\+|-)?(?:180(?:(?:\.0{1,15})?)|(?:1[0-7]\d(?:(?:\.\d{1,15})?)|(?:[1-9]?\d(?:(?:\.\d{1,15})?))))$/;

	let valid = true;

	// function checkTel(tel) {
	// 	tel = tel.replace(/-/gi, "");
	//     var regExp = /^(010{1})[0-9]{3,4}[0-9]{4}$/i;
	//     if (regExp.test(tel)) {
	//         return true;
	//     } else {
	//         return false;
	//     }
	// }
	function checkBrthDt(brthDt) {
		// dd-MM-yyyy
		const regex = /^(0[1-9]|[12]\d|3[01])(0[1-9]|1[0-2])\d{4}$/;
		return regex.test(brthDt.replace(/\D/g, ''));
	}

	$wrap.find(".data-validate").each(function(){
		let $validElement = $(this);
		let tagName = $(this).get(0).tagName.toUpperCase();
		let value = "";
		let attributeName = "";
		let labelname = $(this).attr("data-valid-name");
		
		if(tagName == "SELECT"){
			
			value = $validElement.find("option:selected").val();
			//document.querySelector('select').style.borderColor="#00967a";
		} else if(tagName == "TEXTAREA") {
			value = $validElement.val();
		} else {
			if($validElement.attr("type").toUpperCase() == "RADIO" || $validElement.attr("type").toUpperCase() == "CHECKBOX"){
				attributeName = $validElement.attr("name");
				value = $wrap.find(`input[type='${$validElement.attr("type")}'][name='${attributeName}']:checked`).val();
			}else{
				value = $validElement.val();
			}
		}
		
		if(typeof $validElement.attr("data-valid-required") !== "undefined" && (isNull(value) || value.replaceAll(" ", "") == "")) {
			// 입력해 주세요.
			let msg = "Por favor, insira. ";
			
			
			if ($validElement.attr("type") != undefined) {
				if($validElement.attr("type").toUpperCase() == "RADIO" 
					|| $validElement.attr("type").toUpperCase() == "CHECKBOX"){
					// 선택해 주세요
					msg = "Por favor, escolha. ";
					
				}
			} else if (tagName == "SELECT") {
				// 선택해 주세요
				msg = "Por favor, escolha. ";
			}
			
			new ModalBuilder().init().alertBody(msg+labelname).footer(4,"Confirmar",function(button, modal){
				$validElement.focus();
				modal.close();
			}).open();
			//modalAlertWrap();			
			valid = false;
			return false;
		}
		
		if(typeof $validElement.attr("data-valid-email") !== "undefined" && value !== '' && !emailRegExp.test(value)) {
			new ModalBuilder().init().alertBody("O e-mail não está no formato").footer(4,"Confirmar",function(button, modal){
				modal.close();
				$validElement.focus();
			}).open();
			//modalAlertWrap();			
			valid = false;
			return false;
		}
		
		// if(typeof $validElement.attr("data-valid-phone") !== "undefined" && !checkTel(value)) {
		// 	//연락처 정보가 올바르지 않습니다.
		// 	new ModalBuilder().init().alertBody("As informações de contato estão incorretas.").footer(4,"Confirmar",function(button, modal){
		// 		modal.close();
		// 		$validElement.focus();
		// 	}).open();
		// 	//modalAlertWrap();
		// 	valid = false;
		// 	return false;
		// }
		if(typeof $validElement.attr("data-valid-maximum") !== "undefined" && value.length > parseInt($validElement.attr("data-valid-maximum"))) {
			let max = $validElement.attr("data-valid-maximum");
			//labelname 은(는) max자리를 넘어갈 수 없습니다.
			new ModalBuilder().init().alertBody(labelname +"não pode ir além de "+max+".").footer(4,'Confirmar',function(button, modal){
				modal.close();
				$validElement.focus();			
			}).open();
			//modalAlertWrap();				
			valid = false;
			return false;
		}
		
		if(typeof $validElement.attr("data-valid-minimum") !== "undefined" && value.length < parseInt($validElement.attr("data-valid-minimum"))) {
			let min = $validElement.attr("data-valid-minimum");
			//labelname+"은(는) "+min+"자리보다 커야합니다."
			new ModalBuilder().init().alertBody(labelname+" deve ser maior que "+min+" dígitos.").footer(4,'Confirmar',function(button, modal){
				modal.close();
				$validElement.focus();			
			}).open();
			//modalAlertWrap();				
			valid = false;
			return false;
		}
		
		if(typeof $validElement.attr("data-valid-maximum-number") !== "undefined" && parseInt(value) > parseInt($validElement.attr("data-valid-maximum-number"))) {
			let max = $validElement.attr("data-valid-maximum-number");
			//labelname+"은(는) "+max+"보다 클 수없습니다."
			new ModalBuilder().init().alertBody(labelname+" não pode ser maior que "+max+".").footer(4,'Confirmar',function(button, modal){
				modal.close();
				$validElement.focus();			
			}).open();
			//modalAlertWrap();				
			valid = false;
			return false;
		}
		
		if(typeof $validElement.attr("data-valid-minimum-number") !== "undefined" && parseInt(value) < parseInt($validElement.attr("data-valid-minimum-number"))) {
			let min = $validElement.attr("data-valid-minimum-number");
			//labelname+"은(는) "+min+"보다 커야합니다."
			new ModalBuilder().init().alertBody(labelname+" deve ser maior que "+min+".").footer(4,"Confirmar",function(button, modal){
				modal.close();
				$validElement.focus();			
			}).open();
			//modalAlertWrap();			
			valid = false;
			return false;
		}

		if(typeof $validElement.attr("data-valid-password") !== 'undefined' && !passwordRegExp.test(value)) {
			new ModalBuilder().init().alertBody("O valor da senha está incorreto.").footer(4,"Confirmar",function(button, modal){
				modal.close();
				$validElement.focus();
			}).open();
			//modalAlertWrap();
			valid = false;
			return false;
		}

		if(typeof $validElement.attr("data-valid-lat") !== 'undefined' && !latitudeRegex.test(value)) {
			// 위도 값이 잘못되었습니다.
			new ModalBuilder().init().alertBody("O valor da latitude está incorreto.").footer(4,"Confirmar",function(button, modal){
				modal.close();
				$validElement.focus();
			}).open();
			valid = false;
			return false;
		}

		if(typeof $validElement.attr("data-valid-lng") !== 'undefined' && !longitudeRegex.test(value)) {
			// 경도 값이 잘못되었습니다.
			new ModalBuilder().init().alertBody("O valor da longitude está incorreto.").footer(4,"Confirmar",function(button, modal){
				modal.close();
				$validElement.focus();
			}).open();
			valid = false;
			return false;
		}

		if (typeof $validElement.attr('data-valid-brth') !== 'undefined' && !checkBrthDt(value)) {
			// 생년월일 형식이 잘못되었습니다.
			new ModalBuilder().init().alertBody("O formato da data de nascimento está incorreto.").footer(4,"Confirmar",function(button, modal){
				modal.close();
				$validElement.focus();			
			}).open();			
			valid = false;
			return false;
		}
		
		if(typeof $validElement.attr("data-valid-startDate") !== "undefined"){
			startDate = value;
		}

		if(typeof $validElement.attr("data-valid-endDate") !== "undefined"){
			endDate = value;
		}
	});
	return valid;
}