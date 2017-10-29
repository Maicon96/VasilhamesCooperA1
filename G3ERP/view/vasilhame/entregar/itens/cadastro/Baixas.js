Ext.define("App.view.vasilhame.entregar.itens.cadastro.Baixas" ,{
    extend: "Ext.panel.Panel", 
    alias : "widget.entregarItemCadBaixa",
    
    requires: [
        "App.view.vasilhame.entregar.itens.baixas.consulta.Grid"
    ],
    
    initComponent: function() {
        var me = this;
        me.title = "Baixas";        
        me.listeners = {
            activate: function(tab, eOpts) {                                      
                if (!tab.down("entregarItemBaixaConGrid").params.NoActivateOnCreate) {
                    tab.down("entregarItemBaixaConGrid").store.currentPage = 1;
                    tab.down("entregarItemBaixaConGrid").store.load();
                }                
                else {
                    tab.down("entregarItemBaixaConGrid").params.NoActivateOnCreate = false;
                }
            }
        };
       
        // Chamada para a função de carregamento dos programas    
        var callProgram = new CallProgram();
        callProgram.programa = "vasilhames.EntregarItemBaixaCon";
        callProgram.show = false;
        var paramsEntregarItemCon = callProgram.call();
        
        paramsEntregarItemCon.fieldsFix = new Array();
        paramsEntregarItemCon.fieldsFix.push({
            field: "idEmpresa", 
            fieldBtn: "conEmpresas", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        paramsEntregarItemCon.fieldsFix.push({
            field: "idFilial", 
            fieldBtn: "conFiliais", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });
        paramsEntregarItemCon.fieldsFix = new Array();
        paramsEntregarItemCon.fieldsFix.push({
            field: "idVasilhameEntregarItem", 
            fieldBtn: "conVasilhamesItens", 
            value: 0,
            type: "int",
            comparison: "eq",
            connector: "AND",
            readOnly: true,
            filterable: true
        });  
        
        paramsEntregarItemCon.columnsHidden = new Array();
        paramsEntregarItemCon.columnsHidden.push({
            dataIndex: "idEmpresa", 
            value: true
        });        
        paramsEntregarItemCon.columnsHidden.push({
            dataIndex: "idFilial", 
            value: true
        });
        paramsEntregarItemCon.columnsHidden.push({
            dataIndex: "idVasilhameEntregarItem", 
            value: true
        });  
        
        // Propriedade para a aba ativa não fazer o load no create da tab
        paramsEntregarItemCon.NoActivateOnCreate = false;
        
        me.items = [
            {
                xtype: "entregarItemBaixaConGrid",
                anchor: "100% 100%",
                params: paramsEntregarItemCon
            }
        ];
        me.callParent(arguments);
        
        var toolbarTop = me.down("entregarItemBaixaConGrid").getDockedItems("toolbar[dock='top']");
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