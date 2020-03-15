package cn.ygl.zkview.controller;

import cn.ygl.zkview.service.ZKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/zk")
public class ZkController {
    @RequestMapping(value="*")
    public String index(Model model){
        return "zkindex";
    }

    @Autowired
    @Qualifier("ZKServiceImpl")
    private ZKService zkService;

    private boolean bAdmin = false;

    @RequestMapping(value="/copydata")
    @ResponseBody
    public String copydata(Model model, @RequestParam(name = "srcZKAddr") String zk1, @RequestParam(name="targetZKAddr") String zk2,
                           @RequestParam(name = "zkPath") String zkPath){

        zkService.copyZKData(zk1, zk2, zkPath);
        return "success";
    }

    @RequestMapping(value="/showzk", method = RequestMethod.POST)
    public String showzk(Model model, @RequestParam String zkAddr){
        zkService.setZkAddr(zkAddr);
        Map<String, String> datas = zkService.getDatas("/");
        model.addAttribute("datas", datas);
        model.addAttribute("path", "/");
        if(zkAddr.equals("127.0.0.1:2181")){
            bAdmin = true;
        }else{
            bAdmin = false;
        }
        model.addAttribute("admin", bAdmin?"true":"false");
        return "/zk/showzk";
    }

    @RequestMapping(value="/get", method = RequestMethod.POST)
    public String get(Model model, @RequestParam String path){
        model.addAttribute("datas", zkService.getDatas(path));
        model.addAttribute("path", path);
        model.addAttribute("admin", bAdmin?"true":"false");
        return "/zk/showzk";
    }

    @RequestMapping(value="/data", method = RequestMethod.POST)
    public String data(Model model, @RequestParam String path, @RequestParam String node){
        String p = path.equals("/")?path:path+"/";
        p = p + node;
        model.addAttribute("path", p);
        model.addAttribute("datas", zkService.getDatas(p));
        model.addAttribute("admin", bAdmin?"true":"false");
        return "/zk/showzk";
    }

    @RequestMapping(value="/create", method = RequestMethod.POST)
    @ResponseBody
    public String create(Model model, @RequestParam String path, @RequestParam String node, @RequestParam String data){
        String p = path.equals("/")?path:path+"/";
        zkService.setPathData(p+node, data);
        return path;
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(Model model, @RequestParam String path, @RequestParam String node){
        String p = path.equals("/")?path:path+"/";
        zkService.deletePath(p+node);
        return path;
    }

    @RequestMapping(value="/change", method=RequestMethod.POST)
    @ResponseBody
    public String change(Model model, @RequestParam String path, @RequestParam String data){
        zkService.setPathData(path, data);

        return "success";
    }

    @GetMapping(value = "/childlistener/{path}/{op}")
    @ResponseBody
    public String childListener(@PathVariable String path, @PathVariable String op){
        return zkService.childListener("/"+path, op)?"success":"failed";
    }
}
