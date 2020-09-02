function loadFeed()
{

    const category = document.getElementById('categories').value;
    document.getElementById("background-im").style.backgroundImage = 'url(/images/' + category + '.jpg)';
    document.getElementById("background-im").style.backgroundRepeat = 'no-repeat';
    document.getElementById("background-im").style.backgroundSize = 'cover';
    document.getElementById("background-im").style.backgroundPosition='center';
    document.getElementById("background-im").style.minHeight='100%'
    const params = new URLSearchParams();
    params.append('Category', category);

   var queryString = params.toString();

    fetch('/CommonFeed/?'+ queryString).then(response => response.json()).then(ideas => {

        // Gets the element to add the comments
         const ideasContainer = document.getElementById('ideas-container');
         ideasContainer.innerHTML = "";
         ideas.forEach((idea) => {
         var ideaDiv = getIdeaDiv(idea);
         ideasContainer.appendChild(ideaDiv);
         });
         

        });
}
function getCardBody(idea){
    var cardBody = document.createElement("div");
    cardBody.className = "card-body";
    
    var cardTitle = getcardTitle(idea);
    var cardDescription = getCardDescription(idea);
    var lineBreak = document.createElement("br");
    var ideaInfoLink = getIdeaInfoLink(idea);
    var statsLink = getStatsLink(idea);

    cardBody.appendChild(cardTitle);
    cardBody.appendChild(cardDescription);
    cardBody.appendChild(lineBreak);
    cardBody.appendChild(ideaInfoLink);
    cardBody.appendChild(statsLink);
    
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

/* Set the width of the side navigation to 250px and the left margin of the page content to 250px */
function openNav() {
  document.getElementById("mySidenav").style.width = "250px";
  document.getElementById("main").style.marginLeft = "250px";
}

/* Set the width of the side navigation to 0 and the left margin of the page content to 0 */
function closeNav() {
  document.getElementById("mySidenav").style.width = "0";
  document.getElementById("main").style.marginLeft = "0";
}
