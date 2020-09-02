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

function getStatsLink(idea){
    var statsLink = document.createElement("button");
    statsLink.className = "btn btn-dark";
    var link = document.createTextNode("Analyse Statistics");
    statsLink.appendChild(link);
    // statsLink.href = "/charts.html";
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
    var statsLink = getStatsLink(idea);
    var lineBreak2 = document.createElement("br");

    cardBody.appendChild(cardTitle);
    cardBody.appendChild(cardDescription);
    cardBody.appendChild(lineBreak);
    cardBody.appendChild(statsLink);
    cardBody.appendChild(lineBreak2);
    return cardBody;
}

function getIdeaCard(idea){
    var ideaCard = document.createElement("div");
    ideaCard.className = "card mb-3 text-center";
    ideaCard.style.width = "22rem";
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


function fetchIdea() {
  productid = localStorage.getItem("productid");
  fetch('/ideaPage?productid=' + productid).then(response => response.json()).then((idea) => {
      console.log("he");
    var ideasContainer = document.getElementById('ideainfo-container');
    // ideasContainer.appendChild(ideaDiv);
    var ideaDiv = getIdeaDiv(idea);
    ideasContainer.appendChild(ideaDiv);
    addComments();
  });
}


function getCommentPara(comment){
    var commentPara = document.createElement("p");
    var commentText = document.createTextNode(comment.text);
    commentPara.appendChild(commentText);
    return commentPara;
}

function getCommentData(comment){
    var blockQuote = document.createElement("blockquote");
    blockQuote.className = "blockquote mb-0";
    blockQuote.appendChild(getCommentPara(comment));
    // blockQuote.appendChild(getCommentFooter(comment));
    return blockQuote;
}

function getCommentDiv(comment){
    var commentDiv = document.createElement("div");
    commentDiv.className = "commentbackground";
    commentDiv.appendChild(getCommentData(comment));    
    return commentDiv;
}

function addComments() {
  productid = localStorage.getItem("productid");
  console.log(productid);
  fetch('/ideaComments?productid=' + productid).then(response => response.json()).then((comments) => {

    var commentsContainer = document.getElementById('comments-container');
    for(var i = 0; i < comments.length; i++){
        var commentDiv = getCommentDiv(comments[i]);
        var lineBreak = document.createElement("br");

        commentsContainer.appendChild(commentDiv);
        commentsContainer.appendChild(lineBreak);
    }

  });
}