<template>
  <div>
    <q-input v-model="form.title" stack-label="Titolo"/>
    <q-input type="textarea" v-model="form.description" stack-label="Descrizione"/>
    <q-input v-model="form.type" stack-label="Genere"/>

    <q-input v-model="form.tags" stack-label="Tags"/>
    <div class="flex flex-center" style="margin:20px">
      <q-rating size="30px" v-model="form.rank" :max="5"/>
    </div>
    <q-uploader @add="addVideoToScope" url float-label="Film"/>
    <div class="flex flex-center" style="margin:20px">
      <q-btn label="Invia" @click="submit"/>
    </div>
  </div>
</template>


<script>
export default {
  name: "AddFilmComponent",
  data() {
    return {
      form: {
        title: "",
        description: "",
        type: "",
        tags: "",
        rank: -1,
        file: new Object()
      }
    };
  },
  methods: {
    addVideoToScope(file) {
      console.log("add video", file);
      this.form.file = file[0];
    },
    async submit() {
      console.log(this.form.file);
      let response = await this.$backend.manage.submitFilm(this.form);
      if (response.data.idFilm && response.data.idFilm != null) {
        this.$q.notify({
          type: "positive",
          message: "Film inserito con successo"
        });
      } else {
        this.$q.notify({
          type: "negative",
          message: "Errore nell' inserimento del film"
        });
      }
    }
  }
};
</script>