Ext.define("App.store.vasilhame.entregues.itens.Itens", {
    extend: "Ext.data.Store",

    requires: [
        "Ext.ux.data.proxy.JsonRest"
    ],
    
    model: "App.model.vasilhame.entregues.itens.Itens",
    pageSize: "15",
    remoteSort: true, 
    remoteFilter: true,
    proxy: {
        type: "jsonrest",
        api: {
            create: BACKEND + "/vasilhame/entregues/itens/cadastro/save",
            read: BACKEND + "/vasilhame/entregues/itens/consulta/load",
            update: BACKEND + "/vasilhame/entregues/itens/cadastro/update",
            destroy: BACKEND + "/vasilhame/entregues/itens/consulta/delete"
        },
        reader: {
            type: "json", 
            rootProperty: "registros"
        },
        writer: {
            type: "json", 
            rootProperty: "registros"              
        }
    }
});
