class HandleNextForm {
  constructor() {}

  passNextForm(selectorName) {
    document.querySelector(`${selectorName} .completeInfo h5`).innerHTML = "";

    let all = document.querySelectorAll(`${selectorName} input`);
    const text = document.querySelector(`${selectorName} .completeInfo h5`);

    for (let element of all) {
      console.log(`check element ${element}`);
      if ((element.value.length == 0) & (text.innerHTML.length == 0)) {
        document
          .querySelector(`${selectorName} .completeInfo h5`)
          .insertAdjacentText(
            "afterbegin",
            "bạn cần điền đầy đủ thông tin hoặc đúng trường"
          );
        return false;
      }
    }

    return true;
  }
}

class HandleButton {
  constructor() {}

  addBackFormUpImg() {
    document.getElementById("upImg").style.left = "-100%";
    document.getElementById("infoBank").style.left = "0";
  }

  addBackFormInfoBank() {
    document.getElementById("infoBank").style.left = "-100%";
    document.getElementById("infoLogin").style.left = "0";
  }

  addNextFormInfoBank(boolPass) {
    if (boolPass) {
      document.getElementById("infoBank").style.left = "-100%";
      document.getElementById("upImg").style.left = "0";

      this.#addImgeNotice(".notice #formUpImage");
    }
  }

  addNextFormReg(boolPass) {
    if (boolPass) {
      document.getElementById("infoLogin").style.left = "-100%";
      document.getElementById("infoBank").style.left = "0";

      this.#addImgeNotice(".notice #formRegister");
    }

    return this;
  }

  #addImgeNotice(selectorName) {
    let change = document.querySelector(`${selectorName} img`);
    if (change == null) {
      document
        .querySelector(`${selectorName}`)
        .insertAdjacentHTML(
          "afterbegin",
          `<img class="imgCheck" src="./image/check.png" alt="success">`
        );
    }
  }
}

class HandleImage {
  #domain = "http://localhost:8080";
  #urlRegis = this.#domain + "/register/driver";
  #urlImg = this.#domain + '/register/upload/img';
  completeReg() {
    var userName = document.querySelector("#nameUser");
    var pass = document.querySelector("#password");
    var email = document.querySelector("#mail");
    var phone = document.querySelector("#phone");

    var nameBank = document.querySelector("#nameBank");
    var idCredit = document.querySelector("#idCredit");
    var cardHolderName = document.querySelector("#cardHolderName");

    var imgPre = document.querySelector('#previousCar');
    var imgBeh = document.querySelector('#behinCar');
    var imgHor = document.querySelector('#horizontalCarr');
    var imgLic = document.querySelector('#licensePlatesCar');
    var imgFac = document.querySelector('#faceDriver');

    var dataImg = new FormData();
    dataImg.append('filePre', imgPre.files.item(0));
    dataImg.append('fileBeh' , imgBeh.files.item(0));

    let optionImg = {
      method :'POST',
      body:dataImg
    }

    fetch(this.#urlImg, optionImg)
    .then(res=> res.text())
    .then(res=> {
      console.log('res img ' +res);
    })
    // fetch(this.#urlImg',{
    //   method: 'POST',
    //   body: formData
    // });

    let option = {
      method: "POST",
      body: JSON.stringify({
        userName: userName.value,
        pass: pass.value,
        email: email.value,
        phone: phone.value,
        nameBank: nameBank.value,
        idCredit: idCredit.value,
        cardHolderName: cardHolderName.value,
      }),
      headers: {
        "Content-type": "application/json; charset=UTF-8",
      },
    };
    
    fetch(this.#urlRegis, option)
      .then((res) => res.text)
      .then((res) => {
        console.log(res);
      });

    
  }

  loadFilePre() {
    this.#loadFile("#lbPrevious", "#previousCar");
  }

  loadFileBehin() {
    this.#loadFile("#lbBehin", "#behinCar");
  }

  loadFileHoriz() {
    this.#loadFile("#lbHoriz", "#horizontalCarr");
  }
  loadFileLicen() {
    this.#loadFile("#lbLicen", "#licensePlatesCar");
  }

  loadFileFaceDr() {
    this.#loadFile("#lbFaceDr", "#faceDriver");
  }

  #loadFile(selector, selectorInput) {
    let change = document.querySelector(selector);
    const inputFile = document.querySelector(selectorInput);

    inputFile.addEventListener("change", handleFiles, false);
    function handleFiles(event) {
      // const fileList = this.files; /* now you can work with the file list */
      inputFile.setAttribute(
        "value",
        URL.createObjectURL(event.target.files[0])
      );
      change.setAttribute("src", URL.createObjectURL(event.target.files[0]));
    }
  }
}

export { HandleNextForm, HandleButton, HandleImage };
