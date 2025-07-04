package com.task.kanri.tools.tasks.SC_01.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task.kanri.tools.tasks.SC_01.bean.SC_01_Torihikisaki;
import com.task.kanri.tools.tasks.SC_01.bean.SC_01_TorihikisakiSearchResult;
import com.task.kanri.tools.tasks.SC_01.form.SC_01_CustomerListForm;
import com.task.kanri.tools.tasks.SC_01.repository.SC_01_Repository;
import com.task.kanri.tools.tasks.common.bean.Kubunchi;
import com.task.kanri.tools.tasks.common.repository.KubunchiRepository;

@Service
public class SC_01_SearchService {

    @Autowired
    private SC_01_Repository SC_01_repository;

    @Autowired
    private KubunchiRepository kubunchiRepository;

    public SC_01_CustomerListForm doExecute(SC_01_CustomerListForm form) {

        List<SC_01_TorihikisakiSearchResult> torihikisakiList = SC_01_repository.findAll();
        form.setTorihikisakiList(setSearcuResult(torihikisakiList));
        return form;
    }

    private List<SC_01_Torihikisaki> setSearcuResult(List<SC_01_TorihikisakiSearchResult> torihikisakiResultList) {
        List<SC_01_Torihikisaki> torihikisakiList = new ArrayList<>();

        for (SC_01_TorihikisakiSearchResult rec : torihikisakiResultList) {
            SC_01_Torihikisaki torihikisaki = new SC_01_Torihikisaki();
            torihikisaki.setTorihikisakiId(rec.getTorihikisakiId());
            torihikisaki.setTorihikisakiName(rec.getTorihikisakiName());

            Kubunchi hojinKojinKbn = kubunchiRepository.findByKubunCdAndKubunchiCd("0001", rec.getHojinKojinKbn());

            torihikisaki.setHojinKojinKbn(hojinKojinKbn);
            torihikisakiList.add(torihikisaki);
        }
        return torihikisakiList;
    }
}
