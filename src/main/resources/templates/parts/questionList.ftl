<#include "security.ftl">
<#import "pager.ftl" as p>

<@p.pager page url />

<div class="card-columns" id="question-list">
    <#list page.content as question>
        <div class="card my-3" data-id="${question.id}">
            <#if question.filename??>
                <img src="/img/${question.filename}" class="card-img-top" />
            </#if>
            <div class="m-2">
                <span>${question.text}</span><br/>
                <i>#${question.tag}</i>
            </div>
            <div class="card-footer text-muted container">
                <div class="row">
                    <a class="col align-self-center" href="/user-questions/${question.author.id}">${question.authorName}</a>
                    <a class="col align-self-center" href="/questions/${question.id}/like">
                        <#if question.meLiked>
                            <i class="fas fa-heart"></i>
                        <#else>
                            <i class="far fa-heart"></i>
                        </#if>
                        ${question.likes}
                    </a>
                    <#if question.author.id == currentUserId>
                        <a class="col btn btn-primary" href="/user-questions/${question.author.id}?question=${question.id}">
                            Edit
                        </a>
                    </#if>
                </div>
            </div>
        </div>
    <#else>
        No question
    </#list>
</div>

<@p.pager page url />