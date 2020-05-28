<template>
  <div>
    <q-input
      v-model="currentFilm.title"
      float-label="Film"
      @input="search"
      style="margin-bottom:30px"
    />
    <div v-show="state=='SEARCH'">
      <q-card
        inline
        v-for="film in films"
        :key="film.id"
        style="width: 400px; margin:20px; padding:20px"
        @click.native="play(film)"
      >
        <q-card-media>
          <img v-if="film.image!=null && film.image.length!=0" class="imgcover" :src="film.image">
          <img v-else class="imgcover" src="assets/play-button.svg">
        </q-card-media>
        <q-card-title>
          <div class="flex flex-center">
            <q-rating disable slot="subtitle" v-model="film.rank" :max="5"/>
          </div>
          {{film.title}}
          <div slot="right" class="row items-center">
            <!--<q-icon name="place"/>-->
            {{film.type}}
          </div>
        </q-card-title>
        <q-card-main>
          <p>{{film.tags}}</p>
          <p class="text-faded">{{film.description}}</p>
        </q-card-main>
      </q-card>
    </div>
    <div v-if="state=='PLAY'">
      <div class="text-center">
        <p style="margin:0;padding:0;font-size:40px">{{currentFilm.title}}</p>
      </div>
      <br>
      <div>
        <div class="flex flex-center">
          <video ref="player" controls>
            <source :src="computeVideoSource()">
          </video>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SearchFilmComponent",
  data() {
    return {
      state: "SEARCH",
      films: [],
      currentFilm: {}
    };
  },
  methods: {
    async search(val) {
      const response = await this.$backend.search.findFilm(val);
      this.state = "SEARCH";
      this.films = response.data.films;
    },
    play(film) {
      this.enableGoBack();
      this.currentFilm = film;
      this.$backend.search.view(1, this.currentFilm.id);
      this.state = "PLAY";
    },
    computeVideoSource() {
      return (
        "http://localhost:8100/stream?isFilm=true&id=" + this.currentFilm.id
      );
    },
    enableGoBack() {
      this.$root.$emit("showGoBack");
    },
    hideGoBack() {
      this.$root.$emit("hideGoBack");
    }
  },
  async mounted() {
    const response = await this.$backend.search.getFilms();
    this.films = response.data.films;
    this.$root.$on("goBack", tick => {
      if (this.state == "PLAY") {
        this.state = "SEARCH";
        this.hideGoBack();
      }
    });
  },
  destroyed() {
    this.$root.$off("goBack");
  }
};
</script>


<style>
.imgcover {
  height: 200px;
  object-fit: cover;
}
</style>