const router = new VueRouter({
    routes: [
        {
            name: 'other',
            path: '*',
            redirect: '/',
        },
        {
            name: 'about',
            path: '/about',
            component: httpVueLoader('js/pages/about.vue'),
        },
        {
            name: 'holder',
            path: '/holders/:id',
            component: httpVueLoader('js/pages/holder.vue'),
        },
        {
            name: 'holders',
            path: '/holders/',
            component: httpVueLoader('js/pages/holders.vue'),
        },
        {
            name: 'position',
            path: '/positions/:id',
            component: httpVueLoader('js/pages/position.vue'),
        },
        {
            name: 'positions',
            path: '/positions/',
            component: httpVueLoader('js/pages/positions.vue'),
        },
        {
            name: 'requirements',
            path: '/requirements/',
            component: httpVueLoader('js/pages/requirements.vue'),
        },
        {
            name: 'requisition',
            path: '/requisitions/:id',
            component: httpVueLoader('js/pages/requisition.vue'),
        },
        {
            name: 'requisitions',
            path: '/requisitions/',
            component: httpVueLoader('js/pages/requisitions.vue'),
        },
        {
            name: 'role',
            path: '/roles/:id',
            component: httpVueLoader('js/pages/role.vue'),
        },
        {
            name: 'roles',
            path: '/roles/',
            component: httpVueLoader('js/pages/roles.vue'),
        },
        {
            name: 'signin',
            path: '/signin',
            component: httpVueLoader('js/pages/signin.vue'),
        },
        {
            name: 'signup',
            path: '/signup',
            component: httpVueLoader('js/pages/signup.vue'),
        },
        {
            name: 'start',
            path: '/',
            component: httpVueLoader('js/pages/start.vue'),
        },
        {
            name: 'user',
            path: '/users/:id',
            component: httpVueLoader('js/pages/user.vue'),
        },
        {
            name: 'users',
            path: '/users/',
            component: httpVueLoader('js/pages/users.vue'),
        },
    ]
});
