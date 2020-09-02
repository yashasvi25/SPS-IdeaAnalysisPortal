// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


function getImage(info){
    var img = document.createElement("img"); 
    // img.className = "card-img-top";
    if(info.imageUrl){
        img.src =  info.imageUrl;
    }
    else{
        img.src="/images/usericon.jpg";
    }
    img.style.borderRadius="50%";
    img.style.width = "100"
    img.style.height = "100"
    return img;
}

function getUnitInfo(s1, s2){
    var row = document.createElement("div");
    row.className="row";
    var col1 = document.createElement("div");
    col1.className="col-md-6";
    var label = document.createElement("label");
    label.innerHTML = s1;
    var col2 = document.createElement("div");
    col1.className="col-md-6";
    var p = document.createElement("p");
    p.innerHTML = s2;

    col1.appendChild(label);
    col2.appendChild(p);
    row.appendChild(col1);
    row.appendChild(col2);
    return row;
}

function getInfo(info){
    var infoDiv = document.createElement("div");
    infoDiv.className="tab-content profile-tab";
    if(info.email){
        infoDiv.appendChild(getUnitInfo("Email:",info.email));
    }
    else{
        infoDiv.appendChild(getUnitInfo("Email:","Not Updated yet"));
    }
    if(info.name){
        infoDiv.appendChild(getUnitInfo("Name:",info.name));
    }
    else{
        infoDiv.appendChild(getUnitInfo("Name:","Not Updated yet"));
    }
    if(info.dob){
        infoDiv.appendChild(getUnitInfo("DOB:",info.dob));
    }
    else{
        infoDiv.appendChild(getUnitInfo("DOB:","Not Updated yet"));
    }
    if(info.teamName){
        infoDiv.appendChild(getUnitInfo("Team Name:",info.teamName));
    }
    else{
        infoDiv.appendChild(getUnitInfo("Team Name:","Not Updated yet"));
    }

    return infoDiv;
}


function getcardTitle(idea){
    var cardTitle = document.createElement("h4");
    cardTitle.className = "card-title";
    var titleText = document.createTextNode(idea.title);
    cardTitle.appendChild(titleText);
    return cardTitle;
}

function getCardDescription(idea){
    var cardDescription = document.createElement("b");
    cardDescription.className = "card-text";
    var descriptionText = document.createTextNode(idea.description);
    cardDescription.appendChild(descriptionText);
    return cardDescription;
}

function getIdeaInfoLink(idea){
    var statsLink = document.createElement("button");
    statsLink.className = "btn btn-dark";
    var link = document.createTextNode("View in Detail");
    statsLink.appendChild(link);
    statsLink.onclick = function () {
        localStorage.setItem("productid",idea.productID);
        location.href = "/IdeaPage.html";
    };
    return statsLink;
}

function getStatsLink(idea){
    var statsLink = document.createElement("button");
    statsLink.className = "btn btn-dark";
    var link = document.createTextNode("Analyse Statistics");
    statsLink.appendChild(link);
    statsLink.onclick = function () {
        localStorage.setItem("productid",idea.productID);
        location.href = "/charts.html";
    };
    return statsLink;
}

function getCardBody(idea){
    var cardBody = document.createElement("div");
    cardBody.className = "card-body";
    
    var cardTitle = getcardTitle(idea);
    var cardDescription = getCardDescription(idea);
    var lineBreak = document.createElement("br");
    var ideaInfoLink = getIdeaInfoLink(idea);
    var statsLink = getStatsLink(idea);
    var lineBreak2 = document.createElement("br");

    cardBody.appendChild(cardTitle);
    cardBody.appendChild(cardDescription);
    cardBody.appendChild(lineBreak);
    cardBody.appendChild(ideaInfoLink);
    cardBody.appendChild(statsLink);
    cardBody.appendChild(lineBreak2);
    return cardBody;
}

function getIdeaCard(idea){
    var ideaCard = document.createElement("div");
    ideaCard.className = "card mb-3 text-center";
    ideaCard.style.width = "20rem";
    ideaCard.style.margin = "1.5em";
    if(idea.imageUrl){
        var img = document.createElement("img"); 
        img.className = "card-img-top";
        img.src =  idea.imageUrl;
        img.width = "400"
        img.height = "300"
        ideaCard.appendChild(img);
    }
    ideaCard.appendChild(getCardBody(idea));
    return ideaCard;
}

function getIdeaDiv(idea){
    var ideaDiv = document.createElement("div");
    ideaDiv.style.display = "inline-block";
    ideaDiv.appendChild(getIdeaCard(idea));    
    return ideaDiv;
}

function addIdeasToDashboard() {

  fetch('/product-idea').then(response => response.json()).then((ideas) => {

    var ideasContainer = document.getElementById('ideas-container');
    for(var i = 0; i < ideas.length; i++){
        var ideaDiv = getIdeaDiv(ideas[i]);
        ideasContainer.appendChild(ideaDiv);
    }
      fetch('/profile-info').then(response => response.json()).then((info) => {

    var infoContainer = document.getElementById('info-container');    
    infoContainer.appendChild(getImage(info));
    var lineBreak = document.createElement("br");
    var lineBreak2 = document.createElement("br");

    infoContainer.appendChild(lineBreak);
    infoContainer.appendChild(lineBreak2);
    infoContainer.appendChild(getInfo(info));
    fetchBlobstoreUrlAndShowForm();
    fetchBlobstoreUrlAndShowIdeaForm();

  });

  });
}

function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-profile-upload-url')
      .then((response) => {
        return response.text();
      })
      .then((uploadUrl) => {
        const messageForm = document.getElementById('updateinfo-form');
        messageForm.action = uploadUrl;
      });
}

function fetchBlobstoreUrlAndShowIdeaForm() {
  fetch('/blobstore-idea-upload-url')
      .then((response) => {
        return response.text();
      })
      .then((uploadUrl) => {
        const messageForm = document.getElementById('postidea-form');
        messageForm.action = uploadUrl;
      });
}

$(function(){
    $('select').selectpicker();
});