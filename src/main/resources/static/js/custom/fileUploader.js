/**
 * FileUploader js
 * @param elementId
 * @param isDropDown
 * @param isMultiple
 * @constructor
 */
const FileUploader = function({elementId, isDropDown, isMultiple}) {
    const element = document.getElementById(elementId);
    const fileInput = element.querySelector("input[type='file']");
    const fileList = element.querySelector(".file-list");
    const dragArea = element.querySelector(".drag-area");
    const uploadBtn = element.querySelector(".upload-btn");

    // 드래그 앤 드롭 이벤트 설정
    dragArea.addEventListener("dragover", function(e) {
        e.preventDefault();
        this.style.backgroundColor = "#737373";
    });

    dragArea.addEventListener("dragleave", function(e) {
        e.preventDefault();
        this.style.backgroundColor = "transparent";
    });

    dragArea.addEventListener("drop", function(e) {
        e.preventDefault();
        this.style.backgroundColor = "transparent";

        let files = e.dataTransfer.files;
        if (files.length > 0) {
            handleFileSelection(files);
        }
    });

    // 파일 선택 버튼 클릭 이벤트 설정
    uploadBtn.addEventListener("click", function() {
        fileInput.click();
    });

    // 파일 입력 요소 변경 이벤트 설정
    fileInput.addEventListener("change", function() {
        let files = fileInput.files;
        if (files.length > 0) {
            handleFileSelection(files);
        }
    });

    /**
     * 파일 선택 처리 함수
     * @param {FileList} files - 선택된 파일 목록
     */
    function handleFileSelection(files) {
        const inputFile = fileInput.files;

        if (isMultiple && inputFile.length > 0) {
            if (!fileUploadChange(inputFile) || !fileUploadChange(files)) {
                fileInput.files = inputFile;
                return;
            }
            fileChangeAddEvent(inputFile, files);
        } else {
            if (!fileUploadChange(files)) {
                fileInput.files = inputFile;
                return;
            }
            fileChangeDefaultEvent(files);
        }
        element.querySelector('.upload_wrap').classList.remove('none');
    }

    /**
     * 기존 파일에 새로운 파일 추가 이벤트 처리
     * @param {FileList} inputFileArr - 기존 파일 목록
     * @param {FileList} addFileArr - 추가된 파일 목록
     */
    function fileChangeAddEvent(inputFileArr, addFileArr) {
        const dataTransfer = new DataTransfer();
        Array.from(inputFileArr).forEach(file => {
            addUniqueFile(file, dataTransfer);
        });
        Array.from(addFileArr).forEach(file => {
            addUniqueFile(file, dataTransfer);
        });
        handleFiles(dataTransfer.files);
        fileInput.files = dataTransfer.files;
    }

    /**
     * 기본 파일 변경 이벤트 처리
     * @param {FileList} files - 선택된 파일 목록
     */
    function fileChangeDefaultEvent(files) {
        handleFiles(files);
        fileInput.files = files;
    }

    /**
     * 중복되지 않는 파일 추가
     * @param {File} file - 파일 객체
     * @param {DataTransfer} dataTransfer - DataTransfer 객체
     */
    function addUniqueFile(file, dataTransfer) {
        const uniqueFileSet = new Set();
        const uniqueKey = file.name + "_" + file.size;

        for (let i = dataTransfer.items.length - 1; i >= 0; i--) {
            const existingFile = dataTransfer.items[i].getAsFile();
            if (uniqueFileSet.has(existingFile.name + "_" + existingFile.size)) {
                dataTransfer.items.remove(i);
            } else {
                uniqueFileSet.add(existingFile.name + "_" + existingFile.size);
            }
        }

        if (!uniqueFileSet.has(uniqueKey)) {
            uniqueFileSet.add(uniqueKey);
            dataTransfer.items.add(file);
        }
    }

    /**
     * 파일 목록 처리
     * @param {FileList} files - 파일 목록
     */
    function handleFiles(files) {
        if (files.length === 0) return;
        fileList.innerHTML = "";
        Array.from(files).forEach(file => {
            let fileNm = file.name;
            let uploadList = document.createElement('div');
            uploadList.classList.add('upload_list');
            uploadList.innerHTML = `
                <div class="list_item input_same group_box flex-center">
                    <div class="file_list">${fileNm}</div>
                    <button type="button" data-value="${fileNm}" class="fileDelBtn">
                        <img src="/images/upload_close.png" alt="업로드파일 삭제">
                    </button>
                </div>`;
            fileList.appendChild(uploadList);
        });

        document.querySelectorAll(".fileDelBtn").forEach(button => {
            button.addEventListener('click', function() {
                const fileNm = this.getAttribute('data-value');
                const dataTransfer = new DataTransfer();
                let fileArray = Array.from(fileInput.files);

                this.closest('.upload_list').remove();
                fileArray.filter(file => file.name !== fileNm).forEach(file => {
                    dataTransfer.items.add(file);
                });
                fileInput.files = dataTransfer.files;

                if (fileList.children.length <= 0) {
                    element.querySelector('.upload_wrap').classList.add('none');
                }
            });
        });
    }

    /**
     * 파일 업로드 검증 함수
     * @param {FileList} files - 파일 목록
     * @returns {boolean} - 검증 결과
     */
    function fileUploadChange(files) {
        const maxFileSizeMB = 5;
        const maxBytes = maxFileSizeMB * 1024 * 1024;
        const maxFileNameLength = 50;
        const allowedExtensions = ['doc', 'docx', 'xls', 'xlsx', 'pdf'];

        for (let i = 0; i < files.length; i++) {
            const fileNm = files[i].name;
            const fileBytes = files[i].size;

            if (fileNm !== '') {
                const fileReg = new RegExp("(.*?)\\.(" + allowedExtensions.join("|") + ")$");
                if (!fileReg.test(fileNm)) {
                    showAlert(`Por favor, verifique a extensão do arquivo. (${allowedExtensions.join(", ")})`);
                    return false;
                }

                if (fileBytes > maxBytes) {
                    showAlert(`O tamanho do arquivo não pode exceder ${maxFileSizeMB}MB.`);
                    return false;
                } else if (fileNm.length > maxFileNameLength) {
                    showAlert(`Os títulos dos arquivos não podem exceder ${maxFileNameLength} caracteres.`);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 경고 메시지 표시 함수
     * @param {string} message - 경고 메시지
     */
    function showAlert(message) {
        alert(message);
    }
};