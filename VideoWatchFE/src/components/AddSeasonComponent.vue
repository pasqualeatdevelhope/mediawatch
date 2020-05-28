<template>
  <div>
    <q-input v-model="currentWrite" float-label="Serie" @input="search"/>
    <q-list v-for="serie in series" :key="serie.value" style="z-index:1000">
      <q-item @click.native="setSerie(serie.value,serie.label)">
        <q-item-main>{{serie.label}}</q-item-main>
      </q-item>
    </q-list>
    <q-input type="textarea" v-model="form.description" float-label="Descrizione"/>
    <q-input v-model="form.number" float-label="Numero stagione"/>
    <q-uploader @add="addImageToScope" url float-label="Immagine"/>
    <div class="flex flex-center" style="margin:20px">
      <q-btn label="Invia" @click="submit"/>
    </div>
  </div>
</template>

<script>
export default {
  name: "AddSeasonComponent",
  data() {
    return {
      form: {
        idSerie: -1,
        description: "",
        number: -1,
        image: ""
      },
      series: [],
      currentWrite: ""
    };
  },
  methods: {
    async search(val) {
      if (val.length > 2) {
        let response = await this.$backend.manage.filterSeries(val);
        this.series = response.data;
      } else {
        this.series = [];
      }
    },
    addImageToScope(file) {
      this.form.image = file[0].__img.currentSrc;
    },
    async submit() {
      const response = await this.$backend.manage.createSeason(this.form);
      if (response.data.idSeason && response.data.idSeason != null) {
        this.$q.notify({ type: "positive", message: "Create con successo" });
      } else {
        if (response.data.errorMessage == "ALREDY_EXISTS") {
          this.$q.notify({
            type: "warning",
            message: "Stagione già presente"
          });
        } else if (response.data.errorMessage == "INVALID_SERIE") {
          this.$q.notify({
            type: "warning",
            message: "La serie non esiste"
          });
        } else {
          this.$q.notify({
            type: "negative",
            message: "Si è verificato un problem"
          });
        }
      }
    },
    setSerie(value, label) {
      this.currentWrite = label;
      this.form.idSerie = value;
      this.series = [];
    }
  }
};
</script>