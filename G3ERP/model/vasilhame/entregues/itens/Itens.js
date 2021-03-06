Ext.define("App.model.vasilhame.entregues.itens.Itens",{
    extend: "Ext.data.Model",
    idProperty: "idEmpresa" + "idFilial" + "id",
    fields: [
        {name: "idEmpresa", type: "int", label: "Empresa"},
        {name: "empresa.nome", mapping: "empresa.nome", type: "string", label: "Nome Empresa"},
        {name: "idFilial", type: "int", label: "Filial"},
        {name: "filial.descricao", mapping: "filial.descricao", type: "string", label: "Descrição da Filial"},
        {name: "idCodigo", type: "int", label: "ID"},
        {name: "idVasilhameEntregue", type: "int", label: "ID Vasilhame"},
        {name: "vasilhameEntregue.idPessoa", mapping: "vasilhameEntregue.idPessoa", type: "int", label: "Pessoa"},
        {name: "vasilhameEntregue.pessoa.digito", mapping: "vasilhameEntregue.pessoa.digito", type: "string", label: "Dígito Pessoa"},
        {name: "vasilhameEntregue.nome", mapping: "vasilhameEntregue.nome", type: "string", label: "Nome Pessoa"},
        {name: "vasilhameEntregue.tipoContribuinte", mapping: "vasilhameEntregue.tipoContribuinte", type: "int", allowNull: true, label: "Contribuinte Vasilhame Entregue"},
        {name: "vasilhameEntregue.cpfcnpj", mapping: "vasilhameEntregue.cpfcnpj", type: "string", label: "CPF/CNPJ Vasilhame Entregue"},
        {name: "vasilhameEntregue.situacao", mapping: "vasilhameEntregue.situacao", type: "int", allowNull: true, label: "Situação Vasilhame Entregue"},
        {name: "vasilhameEntregue.idUsuario", mapping: "vasilhameEntregue.idUsuario", type: "string", label: "Usuário Vasilhame Entregue"},
        {name: "vasilhameEntregue.usuario.nome", mapping: "vasilhameEntregue.usuario.nome", type: "string", label: "Nome Usuário Vasilhame Entregue"},
        {name: "idProduto", type: "int", label: "Produto"},
        {name: "produto.digito", mapping: "produto.digito", type: "string", label: "Dígito do Produto"},
        {name: "produto.descricao", mapping: "produto.descricao", type: "string", label: "Descrição do Produto"},
        {name: "quantidade", type: "float", label: "Quantidade"},
        {name: "quantidadeBaixada", type: "float", label: "Quantidade Baixada"},
        {name: "quantidadeBaixar", type: "float", label: "Quantidade a Baixar"},        
        {name: "tipoGarrafeira", type: "int", allowNull: true, label: "Garrafeira"},
        {name: "situacao", type: "int", allowNull: true, label: "Situação Item"},
        {name: "dataHoraGravacao", type: "date", dateFormat: "Y-m-d H:i:s", label: "Data/Hora Gravação"},
        {name: "idUsuario", type: "string", label: "Usuário"},
        {name: "usuario.nome", mapping: "usuario.nome", type: "string", label: "Nome Usuário"}
    ]
});