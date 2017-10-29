Ext.define("App.model.vasilhame.entregar.Entregar",{
    extend: "Ext.data.Model",
    idProperty: "idEmpresa" + "idFilial" + "id",
    fields: [
        {name: "idEmpresa", type: "int", label: "Empresa"},
        {name: "empresa.nome", mapping: "empresa.nome", type: "string", label: "Nome Empresa"},
        {name: "idFilial", type: "int", label: "Filial"},
        {name: "filial.descricao", mapping: "filial.descricao", type: "string", label: "Descrição da Filial"},
        {name: "idCodigo", type: "int", label: "ID"},
        {name: "idPessoa", type: "int", label: "Pessoa"},
        {name: "pessoa.digito", mapping: "pessoa.digito", type: "string", label: "Dígito Pessoa"},
        {name: "nome", type: "string", label: "Nome Pessoa"},
        {name: "tipoContribuinte", type: "int", allowNull: true, label: "Contribuinte"},
        {name: "cpfcnpj", type: "string", label: "CPF/CNPJ"},
        {name: "observacao", type: "string", label: "Observação"},
        {name: "situacao", type: "int", allowNull: true, label: "Situação"},
        {name: "dataHoraGravacao", type: "date", dateFormat: "Y-m-d H:i:s", label: "Data/Hora Gravação"},
        {name: "idUsuario", type: "string", label: "Usuário"},
        {name: "usuario.nome", mapping: "usuario.nome", type: "string", label: "Nome Usuário"}
    ]
});
