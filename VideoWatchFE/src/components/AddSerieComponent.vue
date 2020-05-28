<template>
  <div>
    <q-input v-model="form.title" stack-label="Titolo"/>
    <q-input type="textarea" v-model="form.description" stack-label="Descrizione"/>
    <q-input v-model="form.tags" stack-label="Tags"/>
    <div class="flex flex-center" style="margin:20px">
      <q-rating size="30px" v-model="form.rank" :max="5"/>
    </div>
    <q-uploader @add="addImageToScope" url float-label="Immagine"/>
    <div class="flex flex-center" style="margin:20px">
      <q-btn label="Invia" @click="submit" />
    </div>
  </div>
</template>


<script>
export default {
  name: "AddSerieComponent",
  data() {
    return {
      form: {
        title: "",
        description: "",
        tags: "",
        rank: -1,
        image: ""
      }
    };
  },
  methods:{
    addImageToScope(file){
      this.form.image = file[0].__img.currentSrc;
    },
    async submit(){
      const response = await this.$backend.manage.createSerie(this.form);
      if(response.data.id && response.data.id!=null){
        this.$q.notify({type:"positive", message:"Create con successo"});
      }else{
        this.$q.notify({type:"negative", message:"Si è verificato un problem"});
      }
    }
  }
};
</script>