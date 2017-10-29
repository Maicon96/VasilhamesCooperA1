Ext.define("App.view.vasilhame.entregues.itens.baixas.cadastro.Cupons" ,{
    extend: "Ext.panel.Panel", 
    alias : "widget.entregueItemBaixaCadCupom",
    
    requires: [
        "App.view.vasilhame.entregues.itens.baixas.cupons.consulta.Grid"
    ],
    
    initComponent: function() {
        var me = this;
        me.title = "Cupons";        
        me.listeners = {
            activate: function(tab, eOpts) {                                      
                if (!tab.down("entregueItemBaixaCupomConGrid").params.NoActivateOnCreate) {
                    tab.down("entregueItemBaixaCupomConGrid").store.currentPage = 1;
                    tab.down("entregueItemBaixaCupomConGrid").store.load();
                }                
                else {
                    tab.down("entregueItemBaixaCupomConGrid").params.NoActivateOnCreate = false;
                }
            }
        };
       
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregueItemBaixaCupomCon";
        callProgram.show = false;
        var paramsEntregueItemBaixaCon = callProgram.call();
        
        paramsEntregueItemBaixaCon.fieldsFix = new Array();
        paramsEntregueItemBaixaCon.fieldsFix.push({
            field: "idEmpresaBaixa", 
            fieldBtn: "conEmpresasBaixas", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        paramsEntregueItemBaixaCon.fieldsFix.push({
            field: "idFilialBaixa", 
            fieldBtn: "conFiliaisBaixas", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        paramsEntregueItemBaixaCon.fieldsFix = new Array();
        paramsEntregueItemBaixaCon.fieldsFix.push({
            field: "idVasilhameEntregueItemBaixa", 
            fieldBtn: "conVasilhamesItensBaixas", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });  
        
        paramsEntregueItemBaixaCon.columnsHidden = new Array();
        paramsEntregueItemBaixaCon.columnsHidden.push({
            dataIndex: "idEmpresaBaixa", 
            value: true
        });        
        paramsEntregueItemBaixaCon.columnsHidden.push({
            dataIndex: "idFilialBaixa", 
            value: true
        });
        paramsEntregueItemBaixaCon.columnsHidden.push({
            dataIndex: "idVasilhameEntregueItemBaixa", 
            value: true
        });  
        
        // Propriedade para a aba ativa não fazer o load no create da tab
        paramsEntregueItemBaixaCon.NoActivateOnCreate = false;
        
        me.items = [
            {
                xtype: "entregueItemBaixaCupomConGrid",
                anchor: "100% 100%",
                params: paramsEntregueItemBaixaCon
            }
        ];
        me.callParent(arguments);
        
        var toolbarTop = me.down("entregueItemBaixaCupomConGrid").getDockedItems("toolbar[dock='top']");
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