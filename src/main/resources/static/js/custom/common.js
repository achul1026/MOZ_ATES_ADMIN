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
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth() + 1; // 현재 월
    const date = today.getDate(); // 현재 날짜
	

    let lastMonthYear, lastMonth;
    if (type === 'monthly') {
        // 한 달 전 날짜 계산
        lastMonthYear = year;
        lastMonth = month - 1;
        if (lastMonth === 0) {
            lastMonth = 12;
            lastMonthYear -= 1;
        }
    }

    if (type === 'true') { // 시간대별
    	document.querySelector('.time-today-on').style.display="block";
    	document.querySelector('.time-sted-change').style.display="none";
        document.getElementById('timeLineInput').value = today.toISOString().slice(0, 10); // 오늘 날짜 설정
    } else if (type === 'false') { // 일별
        const yesterday = new Date(today);
        document.querySelector('.time-today-on').style.display="none";
    	document.querySelector('.time-sted-change').style.display="block";
        yesterday.setDate(yesterday.getDate() - 1); // 어제 날짜 구하기
        document.getElementById('sDate').value = yesterday.toISOString().slice(0, 10); // 어제 날짜 설정
        document.getElementById('eDate').value = today.toISOString().slice(0, 10); // 오늘 날짜 설정
    } else if (type === 'monthly') { // 월간
        const lastMonthDate = new Date(year, month - 2, date);
         document.querySelector('.time-today-on').style.display="none";
    	document.querySelector('.time-sted-change').style.display="block";
        document.getElementById('sDate').value = lastMonthDate.toISOString().slice(0, 10); // 이전 달 설정
        document.getElementById('eDate').value = today.toISOString().slice(0, 10); // 오늘 날짜 설정
    }
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
	return value.replace(/[^-\.0-9]/g, '');
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

var fileNo = 0;
var filesArr = new Array();
var deleteFilesArr = new Array();

function addFiles(obj){
	var fileMaxCnt = 5;
	var fileCnt = $("#image_container div").length;
	var remainFileCnt = fileMaxCnt - fileCnt;
	var curFileCnt = obj.files.length;
	
	if (curFileCnt > remainFileCnt) {
       return alert("첨부파일은 최대 " + fileMaxCnt + "개 까지 첨부 가능합니다.");
    }
	
	for (var i = 0; i < Math.min(curFileCnt, remainFileCnt); i++) {

        const file = obj.files[i];

        // 첨부파일 검증
        if (validation(file)) {
            // 파일 배열에 담기
            var reader = new FileReader();
            reader.onload = function () {
                filesArr.push(file);
            };
            reader.readAsDataURL(file)

            // 목록 추가
            let htmlData = '';
            htmlData += '<div id="file' + fileNo + '" class="filebox">';
            htmlData += '   <p class="name">' + file.name + '</p>';
            htmlData += '   <button type="button" onclick="deleteFile(' + fileNo + ');"></button>';
            htmlData += '</div>';
            $('#image_container').append(htmlData);
            fileNo++;
        } else {
            continue;
        }
    }
    // 초기화
    $("input[type=file]").value = "";
}

function validation(obj){
	const fileExtension = obj.name.split('.').pop().toLowerCase();
    const fileTypes = ['gif', 'jpeg', 'png', 'webm', 'avi', 'mp4', 'pdf', 'xlsx', 'xlx', 'word', 'doc', 'zip'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (100 * 1024 * 1024)) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(fileExtension)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}

function deleteFile(num) {
	if(!isNull($("#fileNo"+num).data("value"))){
		deleteFilesArr.push($("#fileNo"+num).data("value"));
	}
    document.querySelector("#file" + num).remove();
    
    const dataTransfer = new DataTransfer();
    let files = $("#uploadFiles")[0].files;
    let fileArray = Array.from(files);
    filesArr[num].is_delete = true;
    filesArr.filter(file => file.is_delete != true).forEach(file => { dataTransfer.items.add(file); });
    $("#uploadFiles")[0].files = dataTransfer.files;
    
    fileNo--;
    
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

function fileDrag() {
	//file upload
        let $dragArea = $("#dragArea");
        let $fileInput = $("#uploadFiles");
        let $fileList = $("#upload_list_box");

        $dragArea.on("dragover", function(e) {
            e.preventDefault();
            $(this).css("background-color", "#737373");
        });

        $dragArea.on("dragleave", function(e) {
            e.preventDefault();
            $(this).css("background-color", "transparent");
        });

        $dragArea.on("drop", function(e) {
            const inputFile = $fileInput[0].files;
            let files = e.originalEvent.dataTransfer.files;
            e.preventDefault();
            $(this).css("background-color", "transparent");
            
            if(inputFile.length > 0){
                if(!fileUploadChange(inputFile)|| !fileUploadChange(files)){
                    $fileInput.prop('files',inputFile);
                    return;
                }
                fileChangeAddEvent(inputFile,files);
            } else{
                if(!fileUploadChange(files)){
                    $fileInput.prop('files',inputFile);
                    return;
                }
                fileChangeDefaultEvent(files);
            }
            $('.upload_wrap').removeClass('none')
        });

    
            const inputFile = $fileInput[0].files;
            $fileInput.click();
            $fileInput.off().on("change", function() {
                if(!fileUploadChange(inputFile) || !fileUploadChange(this.files)){
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
            $("#upload_list_box").empty();
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
                console.log($fileList);
                console.log(uploadList);
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
                
                let uploadLength = $('#upload_list_box').children().length;
        		if(uploadLength <= 0){
        			$('.upload_wrap').addClass('none')
        		}
            });
        }


    function fileUploadChange(files){
        var maxBytes = 5242880;
        var fileNmMaxLength = 50;

        for(var i = 0; i < files.length; i++){
            var fileNm = files[i].name;
            var fileBytes = 0;

            if(fileNm != ''){
                fileBytes = files[i].size;
            }
            if(fileNm != ''){
                var ext = fileNm.slice(fileNm.lastIndexOf(".")+1).toLowerCase();
                //if(ext != 'png' && ext != 'jpg' && ext != 'jpeg' && ext != 'mp4'){
				//	alert('JPG, PNG, MP4 파일만 첨부 가능합니다.')
                //    return false;
                //}else 
                if(fileBytes > maxBytes){
                    return false;
                }else if(fileNm.length > fileNmMaxLength){
				//	new ModalBuilder().init().alertBoby('파일 제목은 50자 이상 넘을 수 없습니다.').footer(4,'확인',function(button, modal){modal.close();}).open();
				//	modalAlertWrap();      
                    return false;
                }
            }
        }
        return true;
    }
}

	
loading = function (text = "") {
    const loading = this;
    let loading_cover = null;
    const loading_html = `<div id="loading-cover">
    						 <div id="loadingContainer">
    						 	 <div class="scaling-dots"><div></div><div></div><div></div></div>
               					 <p id="loadingText">${text}</p>
    						 </div>
       					  </div>`;
    loading.start = function () {
        loading_cover = document.createElement('div');
        loading_cover.innerHTML = loading_html;
        document.body.appendChild(loading_cover);
        return loading;
    }
    loading.end = function () {
        loading_cover.remove();
        loading_cover = null;
    }
    return loading;
}

