// =======================
// EFEITO DO SCROLL (SEU CÓDIGO ORIGINAL)
// =======================
window.addEventListener("scroll", function () {
    const header = document.getElementById("mainHeader");

    if (window.scrollY > 50) {
        header.style.backgroundColor = "#222"; 
        header.style.transition = "background-color 0.3s ease";
    } else {
        header.style.backgroundColor = "transparent";
    }
});


// =======================
// CARREGAR DADOS DO USUÁRIO LOGADO
// =======================
document.addEventListener("DOMContentLoaded", () => {

    const idUsuario = localStorage.getItem("idUsuario");

    if (!idUsuario) {
        console.error("Usuário não logado.");
        return;
    }

    fetch(`/api/conta/${idUsuario}`)
        .then(response => response.json())
        .then(usuario => {
            // PUXA OS CAMPOS DO BACKEND PARA O HTML

            if (document.getElementById("nome"))
                document.getElementById("nome").value = usuario.nome;

            if (document.getElementById("email"))
                document.getElementById("email").value = usuario.email;

            if (document.getElementById("cpf"))
                document.getElementById("cpf").value = usuario.cpf;

            if (document.getElementById("telefone"))
                document.getElementById("telefone").value = usuario.telefone || "";

            if (document.getElementById("dataNascimento"))
                document.getElementById("dataNascimento").value = usuario.dataNascimento || "";
        })
        .catch(erro => console.error("Erro ao carregar dados do usuário:", erro));
});
