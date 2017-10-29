Ext.define("App.view.vasilhame.entregues.itens.cadastro.Baixas" ,{
    extend: "Ext.panel.Panel", 
    alias : "widget.entregueItemCadBaixa",
    
    requires: [
        "App.view.vasilhame.entregues.itens.baixas.consulta.Grid"
    ],
    
    initComponent: function() {
        var me = this;
        me.title = "Baixas";        
        me.listeners = {
            activate: function(tab, eOpts) {                                      
                if (!tab.down("entregueItemBaixaConGrid").params.NoActivateOnCreate) {
                    tab.down("entregueItemBaixaConGrid").store.currentPage = 1;
                    tab.down("entregueItemBaixaConGrid").store.load();
                }                
                else {
                    tab.down("entregueItemBaixaConGrid").params.NoActivateOnCreate = false;
                }
            }
        };
       
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregueItemBaixaCon";
        callProgram.show = false;
        var paramsEntregueItemCon = callProgram.call();
        
        paramsEntregueItemCon.fieldsFix = new Array();
        paramsEntregueItemCon.fieldsFix.push({
            field: "idEmpresa", 
            fieldBtn: "conEmpresas", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        paramsEntregueItemCon.fieldsFix.push({
            field: "idFilial", 
            fieldBtn: "conFiliais", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        paramsEntregueItemCon.fieldsFix = new Array();
        paramsEntregueItemCon.fieldsFix.push({
            field: "idVasilhameEntregueItem", 
            fieldBtn: "conVasilhamesItens", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });  
        
        paramsEntregueItemCon.columnsHidden = new Array();
        paramsEntregueItemCon.columnsHidden.push({
            dataIndex: "idEmpresa", 
            value: true
        });        
        paramsEntregueItemCon.columnsHidden.push({
            dataIndex: "idFilial", 
            value: true
        });
        paramsEntregueItemCon.columnsHidden.push({
            dataIndex: "idVasilhameEntregueItem", 
            value: true
        });  
        
        // Propriedade para a aba ativa não fazer o load no create da tab
        paramsEntregueItemCon.NoActivateOnCreate = false;
        
        me.items = [
            {
                xtype: "entregueItemBaixaConGrid",
                anchor: "100% 100%",
                params: paramsEntregueItemCon
            }
        ];
        me.callParent(arguments);
        
        var toolbarTop = me.down("entregueItemBaixaConGrid").getDockedItems("toolbar[dock='top']");
        var btn = toolbarTop[0].getComponent(0);
        toolbarTop[0].remove(btn);

        btn = toolbarTop[0].getComponent(0);
        toolbarTop[0].remove(btn);
        
        btn = toolbarTop[0].getComponent(13);
        toolbarTop[0].remove(btn);

        btn = toolbarTop[0].getComponent(13);
        toolbarTop[0].remove(btn);
    }
});