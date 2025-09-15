window.addEventListener("scroll", function () {
    const header = document.getElementById("mainHeader");

    if (window.scrollY > 50) {
        // altera cor ao passar de 50px de scroll
        header.style.backgroundColor = "#222"; // cor nova
        header.style.transition = "background-color 0.3s ease";
    } else {
        // volta à cor original
        header.style.backgroundColor = "transparent";
    }
});