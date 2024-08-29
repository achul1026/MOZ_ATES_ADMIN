let multilingual = window.Multilingual?window.Multilingual : null;

/**
 	node.empty();
 */
Node.prototype.empty = function(){
    this.innerHTML = "";
}

function targetToggleClass(_this, cls = "on", onFunction, offFunction){
	$(_this).toggleClass(cls);
	if($(_this).hasClass(cls)){
		typeof onFunction == "function" ? onFunction() : void(0);
	}else{
		typeof offFunction == "function" ? v() : void(0);
	}
}

function resetForm(targetFormId){
	$('#'+targetFormId).find("input, select").each(function(){
		switch(this.tagName.toUpperCase()){
			case "INPUT" : $("#searchTxt").val("");
			case "INPUT" : $("#sDate").val("");
			case "INPUT" : $("#eDate").val("");
				this.getAttribute('type')
			break;
			case "SELECT" : $("#searchType option:eq(0)").prop("selected", true);
			case "SELECT" : $("#dateSearchType option:eq(0)").prop("selected", true);
			break;	
		}
		 
	});
}
function sideBarToggle(_this){
		if ($(_this).hasClass("on")) {
			let subMenus = $(_this).siblings();
			for (const menu of subMenus) {
				if (!$(menu).hasClass('opened')) {
					$(menu).slideUp();
					$(menu).parent().find('.arrow').removeClass('on')
				}
			}
			$(_this).removeClass('on');

		} else {
			if(!$(_this).siblings().hasClass("on")){
				$(_this).siblings().slideDown();
				$(_this).find('.arrow').addClass('on')
				$(_this).addClass('on')
	
				$('.sidebar-list-title').not(_this).removeClass('on');
	
				let subMenus = $('.sidebar-list-title').not(_this).siblings();
				for (const menu of subMenus) {
					if (!$(menu).hasClass('opened')) {
						$(menu).slideUp();
						$(menu).parent().find('.arrow').removeClass('on');
					}
				}
			}
		}
}
function dateCheck(showDate){
	 if(showDate == 'true') {
		 $('.time-check-change').hide();
		 $('.input-date').addClass('data-validate');
	 } else {
	    $('.time-check-change').show();
	    $('.input-date').removeClass('data-validate');
	 }
}

function toggle(tg){
	let setting = document.getElementById(tg);
	 if(setting.style.display==='block'){
        setting.style.display='none';
    }else{
        setting.style.display='block';
    }
}

function passWordEye(elementId, eyeChange){
	let eye = document.getElementById(eyeChange);
	let passInput = document.getElementById(elementId);
	if(eye.classList.contains('on')) {
		passInput.type="password";
		eye.classList.remove('on');
	} else{
		passInput.type="text";
		eye.classList.add('on');
	}
}

function filterdateCheck(type) {
    const hasDoubleDateInput = document.querySelector('.time-sted-change') !== null;

    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth(); // 현재 월
    const date = today.getDate(); // 현재 날짜

    const minDate = new Date(today);
    if (type === 'monthly') {
        minDate.setFullYear(today.getFullYear() - 1);
    } else  {
        minDate.setMonth(today.getMonth() - 2);
    }
    const yyyyMin = minDate.getFullYear();
    const mmMin = String(minDate.getMonth() + 1).padStart(2, '0');
    const ddMin = String(minDate.getDate()).padStart(2, '0');
    const todayMinStr = `${yyyyMin}-${mmMin}-${ddMin}`;

    if (type === 'true') { // 시간대별
    	document.querySelector('.time-today-on').style.display="block";
    	document.querySelector('.time-sted-change').style.display="none";
        document.getElementById('timeLineInput').value = today.toISOString().slice(0, 10); // 오늘 날짜 설정
    } else if (type === 'false') { // 일별
        const yesterday = new Date(today);
        if (hasDoubleDateInput) {
            document.querySelector('.time-today-on').style.display="none";
            document.querySelector('.time-sted-change').style.display="block";
        }
        yesterday.setDate(yesterday.getDate() - 1); // 어제 날짜 구하기
        document.getElementById('sDate').value = yesterday.toISOString().slice(0, 10); // 어제 날짜 설정
        document.getElementById('eDate').value = today.toISOString().slice(0, 10); // 오늘 날짜 설정
    } else if (type === 'monthly') { // 월간
        const lastMonthDate = new Date(year, month - 2, date);
        if (hasDoubleDateInput) {
         document.querySelector('.time-today-on').style.display="none";
         document.querySelector('.time-sted-change').style.display="block";
        }
        document.getElementById('sDate').value = lastMonthDate.toISOString().slice(0, 10); // 이전 달 설정
        document.getElementById('eDate').value = today.toISOString().slice(0, 10); // 오늘 날짜 설정
    }

    if (hasDoubleDateInput) document.getElementById('timeLineInput').setAttribute('min', todayMinStr);
    document.getElementById('eDate').setAttribute('min', todayMinStr);
    document.getElementById('sDate').setAttribute('min', todayMinStr);
}

 /*null check*/
 function isNull(v){
	 return (v === undefined ||  v === null) ? true : false;
 }

function openSelectedMenu() {
	let mainMenus = document.getElementsByClassName('sidebar-list-title');
	for(const mainMenu of mainMenus) {
	    if (mainMenu.classList.contains('on')) {
	      $(mainMenu).siblings().slideDown();
	      $(mainMenu).find('.arrow').addClass('on');
	    }
	}
}

window.addEventListener('load', function() {
		openSelectedMenu();
});

/**
 * @author KY.LEE
 * Input Validation
 * @returns
 */

// 숫자 3자리수 마다 comma
function numberComma(number) {
	if(number == null) return 0;
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function kor_format(value){
    return value.replace(/[^ㄱ-힣]/g, '')
}

function keyupKorEvent(_this){
	let value = _this.value;
	_this.value = kor_format(value);
}

function eng_format(value){
    return value.replace(/[^A-Za-z]/g, '');
}

function keyupEngEvent(_this){
	let value = _this.value;
	_this.value = eng_format(value);
}

function keyupNumberEvent(_this){
	let value = _this.value;
	_this.value = number_format(value);
}

function keyupMeticalEvent(_this){
	let value = _this.value;
	_this.value = metical_format(value);
}

function number_format(value){
	return value.replace(/[^\.0-9]/g, '');
}

function numberOnly(event) {
    let input = event.target.value;
    event.target.value = input.replace(/[^0-9]/g, '');
}
function metical_format(value){
    // 숫자, 소수점, 음수 부호를 제외한 모든 문자 제거
    value = value.replace(/[^-\.0-9]/g, '');

    // 소수점 이하 두 자리까지만 유지
    var parts = value.split('.');
    if (parts.length > 1) {
        // 소수점 이하 두 자리까지만 유지하고, 그 이상은 잘라냄
        parts[1] = parts[1].substring(0, 2);
        value = parts[0] + '.' + parts[1];
    }

    return value;
}

function phone_format(num){
    return num.replace(/[^0-9]/g, '')
    .replace(/^(\d{2,3})(\d{3,4})(\d{4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "")
}

function keyupPhoneEvent(_this){
	let value = _this.value;
	_this.value = phone_format(value);
}

function chkSortNo(_this){
	let value = _this.value.replace(/[^0-9]/g, '')
	_this.value = value;
	
	if(_this.value >= 100){
		_this.value = 99;
	}
}

function getPagingHtml(paging,page){
	var pagingHtml =	'<div>'+
							'<ul class="pagination">'+
								'<li><a href="javascript:fnPageMove('+paging.startPage+')" class="first">First</a></li>'+
								'<li class="page-item">'+
									'<a class="first" href="javascript:fnPageMove('+paging.prevBlock+')" aria-label="Previous">'+
										'<i class="page-arrow-left"></i>'+
									'</a>'+
								'</li>';
							for(var i = paging.startPage; i <= paging.endPage; i++){
								if(paging.startPage == 0) i = 1;
								if(i == page){
			pagingHtml +=			'<li><a href="javascript:fnPageMove('+i+')" class="active num">'+i+'</a></li>';
								}else{
			pagingHtml +=			'<li><a href="javascript:fnPageMove('+i+')" class="num">'+i+'</a></li>';						
								}
							}		
			pagingHtml +=  		'<li class="page-item">'+
									'<a class="first" href="javascript:fnPageMove('+paging.nextBlock+')" aria-label="Previous">'+
										'<i class="page-arrow-right"></i>'+
									'</a>'+
								'</li>'+
								'<li><a href="javascript:fnPageMove('+paging.endPage+')" class="last">Last</a></li>'+
			      			'</ul>'+
			    		'</div>';
	return pagingHtml;
}

function formatDate(date) {
    var year = date.getFullYear();
    var month = padNumber(date.getMonth() + 1);
    var day = padNumber(date.getDate());
    var hours = padNumber(date.getHours());
    var minutes = padNumber(date.getMinutes());
    var seconds = padNumber(date.getSeconds());
    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
}

function padNumber(number) {
    if (number < 10) {
        return '0' + number;
    }
    return number;
}

/* 단일 파일 업로드 */
function fileUploadInit(maxFileSizeMB, maxFileNameLength, allowedExtensions) {
    let $dragArea = $("#dragArea");
    let $fileInput = $("#uploadFiles");
    let $fileDiv = $("#fileInfoDiv");

    $dragArea.on("dragover", function(e) {
        e.preventDefault();
        $(this).css("background-color", "#737373");
    });

    $dragArea.on("dragleave", function(e) {
        e.preventDefault();
        $(this).css("background-color", "transparent");
    });

    $dragArea.on("drop", function(e) {
        e.preventDefault();
        $(this).css("background-color", "transparent");
        
        let files = e.originalEvent.dataTransfer.files;
		
        if (files.length > 0) {
            if (!fileUploadChange(files, maxFileSizeMB, maxFileNameLength, allowedExtensions)) {
                return;
            }
            fileChangeEvent(files);
        }
    });

    $("#uploadBtn").off().on("click", function() {
        $fileInput.click();
    });

    // Prevent multiple bindings
    $fileInput.off("change").on("change", function() {
        let files = $fileInput[0].files;
        if (files.length > 0) {
            if (!fileUploadChange(files, maxFileSizeMB, maxFileNameLength, allowedExtensions)) {
                return;
            }
            fileChangeEvent(files);
        }
    });

	function fileChangeEvent(files) {
	    let dataTransfer = new DataTransfer();
	    dataTransfer.items.add(files[0]);
	    handleFiles(files);
	    $fileInput[0].files = dataTransfer.files;
	}

    function handleFiles(files) {
        if (files.length === 0) return;
        $fileDiv.empty();

        let fileNm = files[0].name;
        let uploadList = $(`
            <div id="upFileWrap">
                <div id="upFileCon">
                    ${fileNm}
                     <button type="button" data-value="${fileNm}" class="fileDelBtn">
	                    <img src="/images/upload_close_one.png" alt="업로드파일 삭제">
	                </button>
                </div>
            </div>`);
        $fileDiv.append(uploadList);
        $fileDiv.removeClass('none');
       $dragArea.addClass('none');

        $(".fileDelBtn").off("click").on('click', function() {
            var $this = $(this);
            $this.closest('.upload_list').remove();
            $fileInput.val('');
            $fileDiv.addClass('none');
            $dragArea.removeClass('none');
        });
    }
}
    
/* 다중 파일 업로드*/
function fileDrag(maxFileSizeMB, maxFileCount, maxFileNameLength, allowedExtensions, oldFileNmArr = []) {
	//file upload
    let $dragArea = $("#dragArea");
    let $fileInput = $("#uploadFiles");
    let $fileList = $("#newFilesContainer");

    $dragArea.on("dragover", function(e) {
        e.preventDefault();
        $(this).css("background-color", "#737373");
    });

    $dragArea.on("dragleave", function(e) {
        e.preventDefault();
        $(this).css("background-color", "transparent");
    });

    $dragArea.on("drop", function(e) {
		e.preventDefault();
        const inputFile = $fileInput[0].files;      
        let files = e.originalEvent.dataTransfer.files;     
        // e.preventDefault();
        $(this).css("background-color", "transparent");
        
		// 파일 업로드 가능 개수 체크
		if (inputFile.length + files.length > (maxFileCount - oldFileNmArr.length)) {
			$fileInput.prop('files', inputFile);
			new ModalBuilder().init().alertBody(`Não pode haver mais de ${maxFileCount} arquivos.`).footer(4, 'OK', function (button, modal) {
				// 파일은 최대 ${maxFileCount}개를 넘을 수 없습니다.
				modal.close();
			}).open();
			return;
		}
		
        if(inputFile.length > 0){
            if (!fileUploadChange(inputFile, maxFileSizeMB, maxFileNameLength, allowedExtensions, oldFileNmArr) || 
            	!fileUploadChange(files, maxFileSizeMB, maxFileNameLength, allowedExtensions, oldFileNmArr)) {
                $fileInput.prop('files',inputFile);
                return;
            }
            fileChangeAddEvent(inputFile,files);
        } else{
            if(!fileUploadChange(files, maxFileSizeMB, maxFileNameLength, allowedExtensions, oldFileNmArr)){
                $fileInput.prop('files',inputFile);
                return;
            }
            fileChangeDefaultEvent(files);
        }
        $('.upload_wrap').removeClass('none')
    });
    
	let inputFile;
    $("#uploadBtn").off().on("click", function() {
		inputFile = $fileInput[0].files;
        $fileInput.click();
    });
    
    $fileInput.off().on("change", function() {
		// 파일 업로드 가능 개수 체크
		if (inputFile.length + this.files.length > (maxFileCount - oldFileNmArr.length)) {
			$fileInput.prop('files', inputFile);
			new ModalBuilder().init().alertBody(`Não pode haver mais de ${maxFileCount} arquivos.`).footer(4, 'OK', function (button, modal) {
				// 파일은 최대 ${maxFileCount}개를 넘을 수 없습니다.
				modal.close();
			}).open();
			return;
		}
        if	(!fileUploadChange(inputFile, maxFileSizeMB, maxFileNameLength, allowedExtensions, oldFileNmArr)
    		|| !fileUploadChange(this.files, maxFileSizeMB, maxFileNameLength, allowedExtensions, oldFileNmArr)) {
            $fileInput.prop('files',inputFile);
            return;
        }
        if(inputFile.length > 0){
            fileChangeAddEvent(inputFile,this.files);
        } else{
            fileChangeDefaultEvent(this.files);
        }
        $('.upload_wrap').removeClass('none')
    });

    function fileChangeAddEvent(inputFileArr , addFileArr){
        const dataTransfer = new DataTransfer();
        Array.from(inputFileArr).forEach(file => {
            addUniqueFile(file, dataTransfer);
        });
        Array.from(addFileArr).forEach(file => {
            addUniqueFile(file, dataTransfer);
        });
        handleFiles(dataTransfer.files);
        $fileInput.prop('files',dataTransfer.files);
    }

    function fileChangeDefaultEvent(files){
        handleFiles(files);
        $fileInput.prop('files',files);
    }

    function addUniqueFile(file, dataTransfer) {
        const uniqueFileSet = new Set();
        const uniqueFiles = [];
        const uniqueKey = file.name + "_" + file.size;

        for (let i = dataTransfer.items.length - 1; i >= 0; i--) {
            const file = dataTransfer.items[i].getAsFile();
            if (uniqueFileSet.has(file.name + "_" + file.size)) {
                dataTransfer.items.remove(i);
            } else {
                uniqueFileSet.add(file.name + "_" + file.size);
                uniqueFiles.push(file);
            }
        }
        if (!uniqueFileSet.has(uniqueKey)) {
            uniqueFileSet.add(uniqueKey);
            dataTransfer.items.add(file);
        }
    }

    function handleFiles(files) {
        if (files.length === 0) return;
        $fileList.empty();
        for (let i = 0; i < files.length; i++) {
            let fileNm = files[i].name;
            var uploadList = $(`
                <div class="upload_list">
            		<div class="list_item input_same group_box flex-center">
            			<div class="file_list">
	                		`+fileNm+`
            			</div>
                		<button type="button" data-value='`+fileNm+`' class="fileDelBtn">
								<img src="/images/upload_close.png" alt="업로드파일 삭제">
                		</button>
            		</div>
                </div>`);
            $fileList.append(uploadList);
        }
		 
        $(".fileDelBtn").on('click',function(){
            var $this = $(this);
            var fileNm = $this.data('value');
            const dataTransfer = new DataTransfer();
            let trans = $('#uploadFiles')[0].files;
            let fileArray = Array.from(trans);

            $this.closest('.upload_list').remove();
            fileArray.filter(file => file.name != fileNm).forEach(file => {
                dataTransfer.items.add(file);
            });
            $fileInput.prop('files',dataTransfer.files);
            
            let uploadLength = $('#newFilesContainer').children().length;
    		if(uploadLength <= 0){
    			$('.upload_wrap').addClass('none')
    		}
        });
    }
}

function fileUploadChange(files, maxFileSizeMB, maxFileNameLength, allowedExtensions, oldFileNmArr = []) {
    var maxBytes = maxFileSizeMB * 1024 * 1024;

    for(var i = 0; i < files.length; i++){
        var fileNm = files[i].name;
        var fileBytes = 0;
        
		if (fileNm != '' && oldFileNmArr.length > 0) {
			let isFileDuplicated = false;
			oldFileNmArr.forEach(oldFileNm => {
				if(oldFileNm.toUpperCase() == fileNm.toUpperCase()) {
					isFileDuplicated = true;
					return false;
				}
			});
			if (isFileDuplicated) {
				new ModalBuilder().init().alertBody('Existem arquivos duplicados.').footer(4, 'OK', function(button, modal) {
					// 중복된 파일이 존재합니다.
					modal.close();
				}).open();
				return false;
			}
		}
		
        if(fileNm != ''){
            fileBytes = files[i].size;
        }
        if(fileNm != ''){
            let fileReg = new RegExp("(.*?)\\.(" + allowedExtensions.join("|") + ")$");
			if (!fileReg.test(fileNm)) {
				new ModalBuilder().init().alertBody(`Por favor, verifique a extensão do arquivo.(${allowedExtensions.join(", ")})`)
				.footer(4, 'Confirmar', function(button, modal) {
					// 파일 확장자를 확인 해 주세요.
					modal.close();
				}).open();
				return false;
			}
			
            if(fileBytes > maxBytes){
				// 파일 용량은 5MB를 초과할 수 없습니다.
				new ModalBuilder().init().alertBody(`O tamanho do arquivo não pode exceder ${maxFileSizeMB}MB.`)
				.footer(4, 'Confirmar', function(button, modal) {
					modal.close();
				}).open(); 
                return false;
                
            }else if(fileNm.length > maxFileNameLength){
				// 파일 제목은 ${maxFileNameLength}자 이상을 넘을 수 없습니다.
			    new ModalBuilder().init()
			    .alertBody(`Os títulos dos arquivos não podem exceder ${maxFileNameLength} caracteres.`)
			    .footer(4,'Confirmar',function(button, modal){modal.close();}).open();
                return false;
            }
        }
    }
    return true;
}

class AdminLoading {
	constructor() {
		let loading_cover = null;
		
    }
    
    start = function (text = "loading") {
	    const loading_html = `<div id="loading-cover">
	    						 <div id="loadingContainer">
	    						 	 <div class="scaling-dots"><div></div><div></div><div></div></div>
	               					 <p id="loadingText">${text}</p>
	    						 </div>
	       					  </div>`;
        this.loading_cover = document.createElement('div');
        this.loading_cover.innerHTML = loading_html;
        document.body.appendChild(this.loading_cover);
        
        return this;
    }
    
    end = function () {
        this.loading_cover.remove();
        this.loading_cover = null;
        return this;
    }
}

/**
 * 생년월일 유효성 keyup 이벤트
 */
function keyupDateCheck(event, pattern, separator) {
	// 패턴 및 구분자 유효성 검사
	if (!["yyyyMMdd", "ddMMyyyy", "MMddyyyy"].includes(pattern)) {
		console.warn("keyupDateCheck() --> Invalid Date Pattern");
		event.target.value = '';
		return false;
	}
	if (separator.length > 1) {
		console.warn("keyupDateCheck() --> Separator Length Too Long");
		event.target.value = '';
		return false;
	}

	const inputKey = event.key;
	if (inputKey == "Backspace" || inputKey == "Delete") {
		event.target.value = '';
		return false;
	}

	// 숫자 외 문자 제거
	let dateVal = event.target.value.replace(/\D/g, "");

	// MM과 dd값에 따라 0삽입
	switch (pattern) {
		case "yyyyMMdd":
			// day
			if (dateVal.length == 7 && dateVal.charAt(6) > 3) {
				dateVal = dateVal.slice(0, 6) + 0 + dateVal.slice(6);
			}
			// Month
			if (dateVal.length == 5 && dateVal.charAt(4) > 1) {
				dateVal = dateVal.slice(0, 4) + 0 + dateVal.slice(4);
			}
			break;
		case "ddMMyyyy":
			// day
			if (dateVal.length == 1 && dateVal > 3) {
				dateVal = 0 + dateVal;
			}
			// Month
			if (dateVal.length == 3 && dateVal.charAt(2) > 1) {
				dateVal = dateVal.slice(0, 2) + 0 + dateVal.slice(2);
			}
			break;
		case "MMddyyyy":
			// day
			if (dateVal.length == 3 && dateVal.charAt(2) > 3) {
				dateVal = dateVal.slice(0, 2) + 0 + dateVal.slice(2);
			}
			// Month
			if (dateVal.length == 1 && dateVal > 1) {
				dateVal = 0 + dateVal;
			}
			break;
	}

	// 패턴에 따른 날짜 유효성 검사
	const dayRegex = /^0[1-9]|[12]\d|3[01]$/;
	const monthRegex = /^0[1-9]|1[0-2]$/;
	const today = new Date();
	const yearToday = today.getFullYear();
	let year = "", month = "", day = "";

	let dayValidNum = 0, monthValidNum = 0, yearValidNum = 0;
	if (pattern == "ddMMyyyy") {
		dayValidNum = 6;
		monthValidNum = 2;
		yearValidNum = 4;
	} else if (pattern == "MMddyyyy") {
		dayValidNum = 4;
		monthValidNum = 4;
		yearValidNum = 4;
	}

	// day
	if (dateVal.length >= 8 - dayValidNum) {
		day = dateVal.slice(6 - dayValidNum, 8 - dayValidNum);
		if (!dayRegex.test(day)) {
			event.target.value = '';
			return false;
		}
	}

	// Month
	if (dateVal.length >= 6 - monthValidNum) {
		month = dateVal.slice(4 - monthValidNum, 6 - monthValidNum);
		if (!monthRegex.test(month)) {
			event.target.value = '';
			return false;
		}
		month -= 1; // month starts from 0
	}

	// year
	if (dateVal.length >= 4 + yearValidNum) {
		year = dateVal.slice(0 + yearValidNum, 4 + yearValidNum);
		if (year > yearToday) {
			event.target.value = '';
			return false;
		}
	}

	// 패턴에 따른 날짜 형식화
	let valLength = 0;
	let sliceIdx = 0;
	if (pattern == "yyyyMMdd") {
		valLength = 3;
		sliceIdx = 2;
	}

	if (dateVal.length > 1 + valLength) {
		dateVal = dateVal.slice(0, 2 + sliceIdx) + separator + dateVal.slice(2 + sliceIdx);
	}

	if (dateVal.length > 4 + valLength) {
		dateVal = dateVal.slice(0, 5 + sliceIdx) + separator + dateVal.slice(5 + sliceIdx);
	}

	// 날짜 유효성 검사

	if (dateVal.length > 9) {
		let finalRegex = "";
		switch (pattern) {
			case "yyyyMMdd": finalRegex = /^\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$/; break;
			case "ddMMyyyy": finalRegex = /^(0[1-9]|[12]\d|3[01])(0[1-9]|1[0-2])\d{4}$/; break;
			case "MMddyyyy": finalRegex = /^(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{4}$/; break;
		}
		const dateCheck = new Date(year, month, day);

		if (dateCheck.getMonth() != month
			|| dateCheck.getDate() != day
			|| dateCheck.getFullYear() != year
			|| !(dateCheck < today)
			|| !finalRegex.test(dateVal.replace(/\D/g, ""))
			|| dateCheck === "Invalid Date") {
			event.target.value = '';
			return false;
		}
	}

	event.target.value = dateVal;
}

function toCamelCase(str) {
  return str.replace(/[-_](.)/g, function(match, group1) {
    return group1.toUpperCase();
  });
}

function createAddress(enderesso1, enderesso2, enderesso3, enderesso4) {
    const addressParts = [enderesso1, enderesso2, enderesso3, enderesso4].filter(part => part != null);

    const fullAddress = addressParts.join(' , ');

    return fullAddress;
}

function getFirstKey(map) {
    const keys = Object.keys(map);
    return keys.length > 0 ? keys[0] : null;
}

function trimValues(inputArr) {
	let valid = true;
	inputArr.some(input => {
		let elmnt = document.getElementById(input);
		if (elmnt == null) {
			console.warn(`trimValues() -> Wrong id: ${input}`);
			valid = false;
			return true;
		}
		elmnt.value = elmnt.value.trim();
	})
	return valid;
}
