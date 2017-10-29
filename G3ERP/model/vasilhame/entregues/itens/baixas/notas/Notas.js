Ext.define("App.model.vasilhame.entregues.itens.baixas.notas.Notas",{
    extend: "Ext.data.Model",
    idProperty: "idEmpresaBaixa" + "idFilialBaixa" + "idVasilhameEntregueItemBaixa" + "idEmpresaNota" + "modelo" + "idFilialNota" 
        + "serie" + "numero" + "dataEmissao" + "sequencia",
    fields: [
        {name: "idEmpresaBaixa", type: "int", label: "Empresa Baixa"},
        {name: "empresaBaixa.nome", mapping: "empresaBaixa.nome", type: "string", label: "Nome Empresa da Baixa"},
        {name: "idFilialBaixa", type: "int", label: "Filial Baixa"},
        {name: "filialBaixa.descricao", mapping: "filialBaixa.descricao", type: "string", label: "Descrição da Filial da Baixa"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.idVasilhameEntregue", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.idVasilhameEntregue", type: "int", label: "Vasilhame Entregue"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idPessoa", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idPessoa", type: "int", label: "Pessoa"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.pessoa.digito", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.pessoa.digito", type: "string", label: "Dígito Pessoa"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.nome", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.nome", type: "string", label: "Nome Pessoa"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.tipoContribuinte", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.tipoContribuinte", type: "int", allowNull: true, label: "Contribuinte Vasilhame Entregue"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.cpfcnpj", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.cpfcnpj", type: "string", label: "CPF/CNPJ Vasilhame Entregue"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.situacao", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.situacao", type: "int", allowNull: true, label: "Situação Vasilhame Entregue"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idUsuario", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.idUsuario", type: "string", label: "Usuário Vasilhame Entregue"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.usuario.nome", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.vasilhameEntregue.usuario.nome", type: "string", label: "Nome Usuário Vasilhame Entregue"},
        {name: "vasilhameEntregueItemBaixa.idVasilhameEntregueItem", mapping: "vasilhameEntregueItemBaixa.idVasilhameEntregueItem", type: "int", label: "ID Vasilhame Item"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.idProduto", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.idProduto", type: "int", label: "Produto"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.produto.digito", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.produto.digito", type: "string", label: "Dígito do Produto"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.produto.descricao", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.produto.descricao", type: "string", label: "Descrição do Produto"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.tipoGarrafeira", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.tipoGarrafeira", type: "int", allowNull: true, label: "Garrafeira"},
        {name: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.situacao", mapping: "vasilhameEntregueItemBaixa.vasilhameEntregueItem.situacao", type: "int", allowNull: true, label: "Situação Item"},
        {name: "idVasilhameEntregueItemBaixa", type: "int", label: "ID da Baixa"},
        {name: "vasilhameEntregueItemBaixa.tipoBaixa", mapping: "vasilhameEntregueItemBaixa.tipoBaixa", type: "int", allowNull: true, label: "Tipo Baixa"},
        {name: "idEmpresaNota", type: "int", label: "Empresa Nota"},
        {name: "empresaNota.nome", mapping: "empresaNota.nome", type: "string", label: "Nome Empresa da Nota"},
        {name: "modelo", type: "string", allowNull: true, label: "Tipo Modelo Nota Fiscal"},
        {name: "idFilialNota", type: "int", label: "Filial Nota Fiscal"},
        {name: "filialNota.descricao", mapping: "filialNota.descricao", type: "string", label: "Descrição da Filial da Nota Fiscal"},
        {name: "serie", type: "int", label: "Série"},
        {name: "numero", type: "int", label: "Número"},
        {name: "dataEmissao", type: "date", dateFormat: "Y-m-d", label: "Data Emissão"},
        {name: "sequencia", type: "float", label: "Sequência"}
    ]
});