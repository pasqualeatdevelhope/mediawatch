export default class {
  constructor(manage) {
    this.manage = manage;
  }

  async createSerie(form) {
    return await this.manage.post("createserie", form);
  }

  async createSeason(form) {
    return await this.manage.post("createseason", form);
  }

  async filterSeries(val) {
    return await this.manage.get("searchserie", { params: { serie: val } });
  }

  async getSeasonBySerie(idSerie){
    return await this.manage.get('searchseason', { params : { idSerie : idSerie} } );
  }

  async submitFilm(form) {
    var formData = new FormData();
    formData.append("file", form.file);
    formData.append("title", form.title);
    formData.append("description", form.description);
    formData.append("type", form.type);
    formData.append("tags", form.tags);
    formData.append("rank", form.rank);
    return await this.manage.post("submitfilm", formData, {
      headers: { Accept: "*/*", "Content-Type": "multipart/form-data" }
    });
  }

  async submitEpisode(form) {
    var formData = new FormData();
    console.log(form);
    formData.append("file", form.file);
    formData.append("idSeason", form.idSeason);
    formData.append("idSerie", form.idSerie);
    formData.append("number", form.number);
    formData.append("title", form.title);
    formData.append("description", form.description);
    formData.append("rank", form.rank);
    console.log('number',form.number);
    return await this.manage.post("submitepisode", formData, {
      headers: { Accept: "*/*", "Content-Type": "multipart/form-data" }
    });
  }
}
