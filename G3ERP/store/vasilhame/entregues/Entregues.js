Ext.define("App.store.vasilhame.entregues.Entregues", {
    extend: "Ext.data.Store",

    requires: [
        "Ext.ux.data.proxy.JsonRest"
    ],
    
    model: "App.model.vasilhame.entregues.Entregues",
    pageSize: "15",
    remoteSort: true, 
    remoteFilter: true,
    proxy: {
        type: "jsonrest",
        api: {
            create: BACKEND + "/vasilhame/entregues/cadastro/save",
            read: BACKEND + "/vasilhame/entregues/consulta/load",
            update: BACKEND + "/vasilhame/entregues/cadastro/update",
            destroy: BACKEND + "/vasilhame/entregues/consulta/delete"
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
