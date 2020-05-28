import { Search, Manage } from "./backend/index";
import axios from 'axios';

export default ({ Vue }) => {
  const search = axios.create({
    baseURL: 'http://192.168.178.100/w/'
  });

  const manage = axios.create({
    baseURL: 'http://192.168.178.100/w/'
  });

  Vue.prototype.$backend = {
    search: new Search(search),
    manage: new Manage(manage)
  };
};
