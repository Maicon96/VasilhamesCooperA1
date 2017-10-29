Ext.define("App.store.vasilhame.entregar.Entregar", {
    extend: "Ext.data.Store",

    requires: [
        "Ext.ux.data.proxy.JsonRest"
    ],
    
    model: "App.model.vasilhame.entregar.Entregar",
    pageSize: "15",
    remoteSort: true, 
    remoteFilter: true,
    proxy: {
        type: "jsonrest",
        api: {
            create: BACKEND + "/vasilhame/entregar/cadastro/save",
            read: BACKEND + "/vasilhame/entregar/consulta/load",
            update: BACKEND + "/vasilhame/entregar/cadastro/update",
            destroy: BACKEND + "/vasilhame/entregar/consulta/delete"
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
