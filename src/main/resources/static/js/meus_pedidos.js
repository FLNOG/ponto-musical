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
// CARREGAR LISTA DE PEDIDOS DO USUÁRIO
// =======================
document.addEventListener("DOMContentLoaded", () => {

    const idUsuario = localStorage.getItem("idUsuario");

    if (!idUsuario) {
        console.error("Usuário não logado.");
        return;
    }

    fetch(`/api/pedidos/${idUsuario}`)
        .then(response => response.json())
        .then(lista => {

            const tabela = document.getElementById("tabela-pedidos");

            lista.forEach(pedido => {
                const linha = `
                    <tr>
                        <td>${pedido.id}</td>
                        <td>${pedido.data}</td>
                        <td>R$ ${pedido.total.toFixed(2)}</td>
                    </tr>
                `;
                tabela.innerHTML += linha;
            });
        })
        .catch(erro => console.error("Erro ao carregar pedidos:", erro));
});
