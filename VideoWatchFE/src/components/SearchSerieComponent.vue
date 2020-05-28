<template>
  <div>
    <q-input v-model="currentSerie" float-label="Serie" @input="search" style="margin-bottom:30px"/>
    <div v-show="state == 'SERIE'">
      <div v-show="series.length!=0">
        <q-card
          inline
          v-for="serie in series"
          :key="serie.id"
          style="width: 400px; margin:20px; padding:20px"
          @click.native="goSeason(serie.id)"
        >
          <q-card-media>
            <img v-if="serie.image!=null && serie.image.length!=0" class="imgcover" :src="serie.image" />
            <img v-else class="imgcover" src="assets/play-button.svg" />          
          </q-card-media>
          <q-card-title>
            <div class="flex flex-center">
              <q-rating disable slot="subtitle" v-model="serie.rank" :max="5"/>
            </div>
            {{serie.title}}
            <div slot="right" class="row items-center">
              <!--<q-icon name="place"/>-->
              {{serie.type}}
            </div>
          </q-card-title>
          <q-card-main>
            <p>{{serie.tags}}</p>
            <p class="text-faded">{{serie.description}}</p>
          </q-card-main>
        </q-card>
      </div>
      <div v-show="series.length==0">
        <p>Nessuna serie trovata</p>
      </div>
    </div>

    <div v-show="state == 'SEASON'">
      <div v-show="seasons.length!=0">
        <q-card
          inline
          v-for="season in seasons"
          :key="season.idSeason"
          style="width: 400px; margin:20px; padding:20px"
          @click.native="goEpisode(season)"
        >
          <q-card-media>
            <img v-if="season.image!=null && season.image.length!=0" class="imgcover" :src="season.image" />
            <img v-else class="imgcover" src="assets/play-button.svg" />          
          </q-card-media>
          <q-card-title>
            <div class="flex flex-center">Stagione {{season.number}}</div>
          </q-card-title>
          <q-card-main>
            <p class="text-faded">{{season.description}}</p>
          </q-card-main>
        </q-card>
      </div>
      <div v-show="seasons.length==0">
        <p>Nessuna stagione trovata</p>
      </div>
    </div>

    <div v-show="state == 'EPISODE'">
      <div v-show="episodes.length!=0">
        <q-card
          inline
          v-for="episode in episodes"
          :key="episode.id"
          style="width: 400px; margin:20px; padding:20px"
          @click.native="play(episode)"
        >
          <q-card-media>
            <img v-if="episode.image!=null && episode.image.length!=0" class="imgcover" :src="episode.image" />
            <img v-else  class="imgcover" src="assets/play-button.svg" />          
          </q-card-media>
          <q-card-title>
            <div class="flex flex-center">
              <q-rating disable slot="subtitle" v-model="episode.rank" :max="5"/>
            </div>
            <div class="flex flex-center">
              Episodio {{episode.number}}
              <br>
              {{episode.title}}
            </div>
            <div slot="right" class="row items-center">
              <!--<q-icon name="place"/>-->
            </div>
          </q-card-title>
          <q-card-main>
            <!--<p>{{season.tags}}</p>-->
            <p class="text-faded">{{episode.description}}</p>
          </q-card-main>
        </q-card>
      </div>
      <div v-show="seasons.length==0">
        <p>Nessuna Episodio trovato</p>
      </div>
    </div>
    <div v-if="state=='PLAY'">
      <div class="text-center">
        <p style="margin:0;padding:0;font-size:40px">{{playingEpisode.title}}</p>
        <p>{{selectedSeason}}X{{playingEpisode.number}}</p>
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
import SearchSeasonStateComponent from "components/SearchSeasonStateComponent";
import SearchEpisodeStateComponent from "components/SearchEpisodeStateComponent";

export default {
  name: "SearcSerieComponent",
  components: {
    SearchSeasonStateComponent,
    SearchEpisodeStateComponent
  },
  data() {
    return {
      state: "SERIE",
      series: [],
      seasons: [],
      episodes: [],
      currentSerie: "",
      selectedSeason: null,
      playingEpisode: null
    };
  },
  methods: {
    async search(val) {
      const response = await this.$backend.search.findSerie(val);
      this.series = response.data.series;
      this.state = "SERIE";
    },
    async goSeason(idSerie) {
      const response = await this.$backend.search.getSeasonBySerie(idSerie);
      this.seasons = response.data.seasons;
      this.state = "SEASON";
    },
    async goEpisode(season) {
      this.selectedSeason = season.number;
      const response = await this.$backend.search.getEpisodeBySeason(
        season.idSeason
      );
      this.episodes = response.data.list;
      this.state = "EPISODE";
    },
    play(episode) {
      this.playingEpisode = episode;
      this.state = "PLAY";
      this.$backend.search.view(0,this.playingEpisode.id);
      setTimeout(()=>{
        let player = this.$refs.player;
        console.log('recording player',player);
        this.startListener(player);
        console.log('starting listener...')
      },500);
    },
    computeVideoSource() {
      return (
        "http://localhost:8100/stream?isFilm=false&id=" + this.playingEpisode.id
      );
    },
    enableGoBack() {
      this.$root.$emit("showGoBack");
    },
    hideGoBack() {
      this.$root.$emit("hideGoBack");
    },
    //VIDEO CONTROLS
    startListener(player){
      player.addEventListener('ended', (event) => {
        console.log('Running ended video listener...')
        setTimeout(()=>{
          const playingNumber = this.playingEpisode.number;
          let found = false;
          for(let i = 0; i< this.episodes.length; i++){
            if(this.episodes[i].number == playingNumber+1){
              this.playingEpisode = this.episodes[i];
              found = true;
              break;
            }
          }
          if(found){
            this.$q.loading.show();
            setTimeout(()=>{
              player.play();
              this.$backend.search.view(0,this.playingEpisode.id);
              this.$q.loading.hide();
              const message = this.selectedSeason+"X"+this.playingEpisode.number+" - "+this.playingEpisode.title;
              this.$q.notify({type:"positive", message:message});
            },3000)
          }else{
            this.$q.notify({type:"warning", message:"Non trovo l' episodio successivo"});
          }
        })
      });
    }
  },
  async mounted() {
    await this.search();
    this.$root.$on("goBack", tick => {
      if (this.state == "PLAY") {
        this.state = "EPISODE";
      } else if (this.state == "EPISODE") {
        this.state = "SEASON";
      } else if (this.state == "SEASON") {
        this.state = "SERIE";
      }
    });
  },
  updated() {
    if (this.state !== "SERIE") {
      this.enableGoBack();
    } else {
      this.hideGoBack();
    }
  },
  destroyed(){
    this.$root.$off('goBack');
  }
};
</script>

<style>
.imgcover{
  height:200px;
  object-fit: cover
}
</style>