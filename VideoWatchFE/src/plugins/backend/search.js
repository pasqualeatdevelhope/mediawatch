export default class {
  constructor(search) {
    this.search = search;
  }

  async getSeasonBySerie(val) {
    return await this.search.get("getseasonbyserie", {
      params: { idSerie: val }
    });
  }

  async getEpisodeBySeason(idSeason) {
    return await this.search.get("getepisodebyseason", {
      params: { idSeason: idSeason }
    });
  }

  async findFilm(val) {
    return await this.search.get("searchfilmbytitle", {
      params: { title: val }
    });
  }

  async findSerie(serie) {
    return await this.search.get("getseries", { params: { serie: serie } });
  }

  view(isFilm, id) {
    console.log('backend',isFilm,id);
    this.search.get("updatelastview", { params: { id: id, isFilm: isFilm } });
  }

  async getFilms(){
    return await this.search.get('getfilms');
  }
}
