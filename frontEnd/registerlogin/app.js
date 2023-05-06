import { HandleNextForm, HandleButton, HandleImage } from "./handleBtn.js";

var handleNextForm = new HandleNextForm();
var handleBtn = new HandleButton();
var handleImg = new HandleImage();

var url = window.location.href;
console.log(`url:${url}`);
console.log(url.includes("index.html"));
if (url.includes("index.html")) {
  let upImg = document.querySelector(".upImage");
  upImg.style.display = "initial";

  let infoBank = document.querySelector(".infoBank");
  infoBank.style.display = "initial";
}

var btnUpImgPre = document.querySelector("#lbPrevious");
if (btnUpImgPre) {
  handleImg.loadFilePre();
}

var btnUpImgBehin = document.querySelector("#lbBehin");
if (btnUpImgBehin) {
  handleImg.loadFileBehin();
}

var btnUpImgHor = document.querySelector("#lbHoriz");
if (btnUpImgHor) {
  handleImg.loadFileHoriz();
}

var btnUpImgLicen = document.querySelector("#lbLicen");
if (btnUpImgLicen) {
  handleImg.loadFileLicen();
}

var btnUpImgFaceDriver = document.querySelector("#lbFaceDr");
if (btnUpImgFaceDriver) {
  handleImg.loadFileFaceDr();
}

var btnNextReg = document.querySelector(".nextFormLogin");
if (btnNextReg) {
  btnNextReg.addEventListener("click", function () {
    let boolPass = handleNextForm.passNextForm("#infoLogin");
    handleBtn.addNextFormReg(boolPass);
  });
}

var nextInfoBank = document.querySelector(".nextFormInfoBank");
if (nextInfoBank) {
  nextInfoBank.addEventListener("click", function () {
    let boolPass = handleNextForm.passNextForm("#infoBank");
    handleBtn.addNextFormInfoBank(boolPass);
  });
}

var backInfoBank = document.querySelector(".backFormInfoBank");
backInfoBank.addEventListener("click", function () {
  handleBtn.addBackFormInfoBank();
});

var completeReg = document.querySelector(".completeRegist");
completeReg.addEventListener("click", function () {
  let boolPass = handleNextForm.passNextForm("#infoBank");
  if (boolPass) {
    console.log('check boolPass complete ' +boolPass );
    handleImg.completeReg();
  }

  // alert(`check boolPass ${boolPass}`);
  // console.log('>> check boolpass completeReg ${boolPass}' +boolPass);
});

var backUpImg = document.querySelector(".backFormUpImg");
if (backUpImg) {
  backUpImg.addEventListener("click", function () {
    handleBtn.addBackFormUpImg();
  });
}
