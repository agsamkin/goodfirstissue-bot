package agsamkin.code.api.github;

import agsamkin.code.model.Language;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;

import lombok.RequiredArgsConstructor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GitHubApi {
    private static final String GITHUB_LANGUAGES_URL = "https://github.com/search/advanced";

    private final GitHub gitHub;

    public List<Language> parseAllLanguagesFromGitHub() {
        List<Language> languages = new ArrayList<>();

        HttpResponse<String> response = Unirest.get(GITHUB_LANGUAGES_URL).asString();
        if (response.getStatus() != HttpStatus.OK) {
            return null;
        }

        final String searchLanguageElement = "search_language";
        final String languageNodeName = "option";

        String body = response.getBody();
        Document doc = Jsoup.parse(body);

        Element searchLanguage = doc.getElementById(searchLanguageElement);
        Elements elements = searchLanguage.getAllElements();

        for (Element element : elements) {
            if (languageNodeName.equals(element.nodeName())) {
                String languageName = element.ownText();
                Language language = Language.builder()
                        .name(languageName).build();
                languages.add(language);
            }
        }
        return languages;
    }

    public void findAllGoodFirstIssue() {
        var res = gitHub.searchIssues().isOpen().q("is:public label:\"good first issue\"").list();
        System.out.println("findAllGoodFirstIssue=" + res.getTotalCount());

    }
}
