class Multilingual {
    constructor() {
        this.translations = {
            "eng": window.engTranslations,
            "por": window.porTranslations
        };
        //기본언어
        this.defaultLang = "por";
        this.lang = this.defaultLang;
        
        this.getLang().then(lang => {
            this.lang = lang;
        }).catch(error => {
            console.error('Fetch error:', error);
        });

    }

    async getLang() {
        try {
            const response = await fetch(window.location.href);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const contentLanguage = response.headers.get('Content-Language');
            return contentLanguage || this.defaultLang;
        } catch (error) {
            throw new Error('Error fetching language:', error);
        }
    }

    getTranslation(key) {
        return this.translations[this.lang][key] || key;
    }
}

window.Multilingual = new Multilingual();