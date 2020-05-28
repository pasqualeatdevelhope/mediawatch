<template>
  <div>
    <q-input v-model="form.title" stack-label="Titolo"/>
    <q-input type="textarea" v-model="form.description" stack-label="Descrizione"/>

    <q-input v-model="currentWriteSerie" float-label="Serie" @input="search"/>
    <q-list v-for="serie in series" :key="serie.value" style="z-index:1000">
      <q-item @click.native="setSerie(serie.value,serie.label)">
        <q-item-main>{{serie.label}}</q-item-main>
      </q-item>
    </q-list>
    <div style="padding-top:15px">
      <q-select
        v-model="selectedSeason"
        :options="season"
        @input="setSeason"
        float-label="Stagione"
      />
    </div>
    <q-input v-model="form.number" stack-label="Numero episodio"/>
    <div class="flex flex-center" style="margin:20px">
      <q-rating size="30px" v-model="form.rank" :max="5"/>
    </div>
    <q-uploader @add="addVideoToScope" url hide-upload-button auto-expand float-label="Video"/>
    <div class="flex flex-center" style="margin:20px">
      <q-btn label="Invia" @click="submit"/>
    </div>
  </div>
</template>


<script>
export default {
  name: "AddEpisodeComponent",
  data() {
    return {
      form: {
        title: "",
        description: "",
        rank: -1,
        idSeason: "",
        idSerie: "",
        number: "",
        file: new Object()
      },
      currentWriteSerie: "",
      series: [],
      season: [],
      selectedSeason: ""
    };
  },
  methods: {
    addVideoToScope(file) {
      this.form.file = file[0];
    },
    async search(val) {
      if (val.length > 2) {
        this.$q.loading.show();
        let response = await this.$backend.manage.filterSeries(val);
        if (response.data.length > 0) {
          this.series = response.data;
          this.$q.loading.show();
        } else {
          this.$q.notify({ type: "warning", message: "Nessuna serie trovata" });
        }
      } else {
        this.series = [];
      }
      if (this.$q.loading.isActive()) {
        this.$q.loading.hide();
      }
    },
    async setSerie(value, label) {
      this.currentWriteSerie = label;
      this.form.idSerie = value;
      const response = await this.$backend.manage.getSeasonBySerie(value);
      this.season = response.data;
      this.series = [];
    },
    setSeason(val) {
      this.form.idSeason = val;
    },
    async submit() {
      let response = await this.$backend.manage.submitEpisode(this.form);
      if (response.data.idEpisode && response.data.idEpisode != null) {
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