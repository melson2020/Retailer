import { Loading } from 'element-ui';

const state = {
    maskloading: false,
    loading:{}
};

const actions = {
    LoadingShow({ commit }) {
        commit("UpdateEmployee");
    }
};

const getters = {
    maskloading: state => state.maskloading,
};

const mutations = {
    showLoading(state) {
        state.loading = Loading.service({
            text: '加载中...',
            lock: true,
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        })
        state.maskloading = true
    },
    hideLoading(state) {
        if (state.maskloading) {
            state.loading.close()
            state.maskloading = false
        }
    },
};

export default {
    state,
    actions,
    getters,
    mutations
};
