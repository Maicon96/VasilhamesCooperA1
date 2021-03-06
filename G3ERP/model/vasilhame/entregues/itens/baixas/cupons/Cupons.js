Ext.define("App.model.vasilhame.entregues.itens.baixas.cupons.Cupons",{
    extend: "Ext.data.Model",
    idProperty: "idEmpresaBaixa" + "idFilialBaixa" + "idVasilhameEntregueItemBaixa" + "idEmpresaCupom" + "idFilialCupom" 
        + "ecf" + "cupom" + "dataEmissao" + "sequencia",
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
        {name: "idEmpresaCupom", type: "int", label: "Empresa Cupom"},
        {name: "empresaCupom.nome", mapping: "empresaCupom.nome", type: "string", label: "Nome Empresa da Cupom"},
        {name: "idFilialCupom", type: "int", label: "Filial Cupom Fiscal"},
        {name: "filialCupom.descricao", mapping: "filialCupom.descricao", type: "string", label: "Descrição da Filial da Cupom Fiscal"},
        {name: "idEcfCupom", type: "int", label: "ECF"},
        {name: "numero", type: "int", label: "Número Cupom Fiscal"},
        {name: "dataEmissao", type: "date", dateFormat: "Y-m-d", label: "Data Emissão"},
        {name: "sequencia", type: "float", label: "Sequência"}
    ]
});