Ext.define("App.view.vasilhame.entregues.itens.baixas.cadastro.Notas" ,{
    extend: "Ext.panel.Panel", 
    alias : "widget.entregueItemBaixaCadNota",
    
    requires: [
        "App.view.vasilhame.entregues.itens.baixas.notas.consulta.Grid"
    ],
    
    initComponent: function() {
        var me = this;
        me.title = "Notas";        
        me.listeners = {
            activate: function(tab, eOpts) {                                      
                if (!tab.down("entregueItemBaixaNotaConGrid").params.NoActivateOnCreate) {
                    tab.down("entregueItemBaixaNotaConGrid").store.currentPage = 1;
                    tab.down("entregueItemBaixaNotaConGrid").store.load();
                }                
                else {
                    tab.down("entregueItemBaixaNotaConGrid").params.NoActivateOnCreate = false;
                }
            }
        };
       
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregueItemBaixaNotaCon";
        callProgram.show = false;
        var paramsEntregueItemCon = callProgram.call();
        
        paramsEntregueItemCon.fieldsFix = new Array();
        paramsEntregueItemCon.fieldsFix.push({
            field: "idEmpresaBaixa", 
            fieldBtn: "conEmpresasBaixas", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        paramsEntregueItemCon.fieldsFix.push({
            field: "idFilialBaixa", 
            fieldBtn: "conFiliaisBaixas", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        paramsEntregueItemCon.fieldsFix = new Array();
        paramsEntregueItemCon.fieldsFix.push({
            field: "idVasilhameEntregueItemBaixa", 
            fieldBtn: "conVasilhamesItensBaixas", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });  
        
        paramsEntregueItemCon.columnsHidden = new Array();
        paramsEntregueItemCon.columnsHidden.push({
            dataIndex: "idEmpresaBaixa", 
            value: true
        });        
        paramsEntregueItemCon.columnsHidden.push({
            dataIndex: "idFilialBaixa", 
            value: true
        });
        paramsEntregueItemCon.columnsHidden.push({
            dataIndex: "idVasilhameEntregueItemBaixa", 
            value: true
        });  
        
        // Propriedade para a aba ativa não fazer o load no create da tab
        paramsEntregueItemCon.NoActivateOnCreate = false;
        
        me.items = [
            {
                xtype: "entregueItemBaixaNotaConGrid",
                anchor: "100% 100%",
                params: paramsEntregueItemCon
            }
        ];
        me.callParent(arguments);
        
        var toolbarTop = me.down("entregueItemBaixaNotaConGrid").getDockedItems("toolbar[dock='top']");
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